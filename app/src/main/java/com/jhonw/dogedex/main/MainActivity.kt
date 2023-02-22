package com.jhonw.dogedex.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.hackaprende.dogedex.machinelearning.Classifier
import com.jhonw.dogedex.*
import com.jhonw.dogedex.R
import com.jhonw.dogedex.WholeImageActivity.Companion.PHOTO_URI_KEY
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.ApiServiceInterceptor
import com.jhonw.dogedex.auth.LoginActivity
import com.jhonw.dogedex.databinding.ActivityMainBinding
import com.jhonw.dogedex.dogdetail.DogDetailActivity
import com.jhonw.dogedex.dogdetail.DogDetailActivity.Companion.IS_RECOGNITION_KEY
import com.jhonw.dogedex.dogdetail.DogDetailComposeActivity
import com.jhonw.dogedex.doglist.DogListActivity
import com.jhonw.dogedex.machinelearning.DogRecognition
import com.jhonw.dogedex.model.Dog
import com.jhonw.dogedex.model.User
import com.jhonw.dogedex.settings.SettingsActivity
import com.jhonw.dogedex.testutils.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.common.FileUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setupCamera()
            } else {
                Toast.makeText(this, "Debes aceptar permisos de la camara", Toast.LENGTH_LONG)
                    .show()
            }
        }

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)

        if (user == null) {
            openLoginActivity()
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }

        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }

        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }

        /*binding.takePhotoFab.setOnClickListener {
            if (isCameraReady)
                takePhoto()
        }*/

        viewModel.status.observe(this) { status ->

            when (status) {
                is ApiResponseStatus.Error -> {
                    //ocultar el progress bar
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_LONG)
                        .show()
                }
                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> binding.loadingWheel.visibility = View.GONE
            }
        }

        viewModel.dog.observe(this) { dog ->
            if (dog != null) {
                openDogDetailActivity(dog)
            }
        }

        viewModel.dogRecognition.observe(this) {
            enabledTakePhotoButton(it)
        }

        requestCameraPermission()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        intent.putExtra(DogDetailComposeActivity.IS_RECOGNITION_KEY, true)
        startActivity(intent)
    }

    /*override fun onStart() {
        super.onStart()
        viewModel.setupClassifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
    }*/

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized)
            cameraExecutor.shutdown()
    }

    private fun setupCamera() {
        binding.cameraPreview.post {//esto lo mete a una fila para esperar que se ejecute
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        //se coloca justo cuando se va a configurar la camara, el EspressoIdlingResource es para la ejecucion de procesos
        EspressoIdlingResource.incremet()
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                //val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                EspressoIdlingResource.decremet()//cuando ya dejamos de analizar la primera imagen
                viewModel.recognizeImage(imageProxy)

                //imageProxy.close()
            }

            cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                imageAnalysis,
                preview
            )

            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun enabledTakePhotoButton(dogRecognition: DogRecognition) {
        if (dogRecognition.confidence > 70.0) {
            binding.takePhotoFab.alpha = 1f
            binding.takePhotoFab.setOnClickListener {
                viewModel.getDogByMLId(dogRecognition.id)
            }
        } else {
            binding.takePhotoFab.alpha = 0.2f
            binding.takePhotoFab.setOnClickListener(null)
        }
    }

    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    // insert your code here.
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    /*val photoUri = outputFileResults.savedUri

                    val bitmap = BitmapFactory.decodeFile(photoUri?.path)
                    classifier.recognizeImage(bitmap)
                    val dogRecognition = classifier.recognizeImage(bitmap).first()
                    viewModel.getDogByMLId(dogRecognition.id)*/
                    //openWholeImageActivity(photoUri.toString())
                }
            })
    }

    private fun openWholeImageActivity(photoUri: String) {
        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(PHOTO_URI_KEY, photoUri)
        startActivity(intent)
    }

    private fun getOutputPhotoFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            filesDir
        }
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setupCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {

                    AlertDialog.Builder(this)
                        .setTitle("Aceptar Permiso")
                        .setMessage("Por favor acepte permiso de la camara")
                        .setPositiveButton("Ok") { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton("Cancel") { _, _ ->
                        }.show()
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        } else {
            setupCamera()
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterial3Api
    private fun openDogListActivity() {
        intent = Intent(this, DogListActivity::class.java)
        startActivity(intent)
    }

    private fun openSettingsActivity() {
        intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}