package com.jhonw.dogedex.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.dogdetail.ui.theme.DogedexTheme
import com.jhonw.dogedex.model.Dog
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@AndroidEntryPoint
class DogDetailComposeActivity : ComponentActivity() {

    companion object {
        const val DOG_KEY = "dog"
        const val MOST_PROBABLE_DOGS_IDS = "most_probable_dogs_ids"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel: DogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //se comenta este fragmento por que ya se envia y recibe la intent con savedStateHandle
        /*val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val probableDogsIds =
            intent?.extras?.getStringArrayList(MOST_PROBABLE_DOGS_IDS) ?: listOf()
        val isRecognition =
            intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_LONG).show()
            finish()
            return
        }*/

        setContent {
            //se comenta esta parte por que ya se llama el detailViewModel desde el DogDetailScreen
            /*val status = viewModel.status
            if (status.value is ApiResponseStatus.Success) {
                finish()
            } else {*/
            DogedexTheme {
                DogDetailScreen(
                    //dog = dog,se envia con savedStateHandle
                    //probableDogIds = probableDogsIds,
                    //isRecognition = isRecognition,
                    //status = status.value,
                    /*onButtonClicked = {
                        onButtonClicked(dog.id, isRecognition)
                    },*/
                    onErrorDialogDismiss = ::resetApiResponseStatus,//este se envia pero igual ya se puede llamar
                    //desde el detailScreen al llamar el hiltViewModel
                    finishActivity = { finish() }
                )
            }
            //}
        }
    }

    private fun resetApiResponseStatus() {
        viewModel.resetApiResponseStatus()
    }

    private fun onButtonClicked(dogId: Long, isRecognition: Boolean) {
        if (isRecognition)
            viewModel.addDogToUser()
        else
            finish()
    }
}