package com.jhonw.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.dogdetail.DogDetailComposeActivity
import com.jhonw.dogedex.dogdetail.ui.theme.DogedexTheme
import com.jhonw.dogedex.model.Dog

@ExperimentalCoilApi
class DogListActivity :
    ComponentActivity() {//se deja de usar appcompatActivity por ComponentActivity para manejar compose

    private val viewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogedexTheme {
                val dogList = viewModel.dogList
                DogListScreen(
                    dogList = dogList.value,
                    onDogClicked = ::openDogDetailActivity)
            }
        }

        /*
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingWheel = binding.loadingWheel

        val recycler = binding.dogRecycler
        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)

        val adapter = DogAdapter()
        adapter.setOnItemClickListener {
            //pasar el dog a DogDetail, para esto se usa el plugin id 'kotlin-parcelize'
            // y se debe parcerlizar el objeto Dog
            val intent = Intent(this, DogDetailComposeActivity::class.java)
            intent.putExtra(DogDetailComposeActivity.DOG_KEY, it)
            startActivity(intent)
        }

        /*adapter.setLongOnItemClickListener {
            dogListViewModel.addDogToUser(it.id)
        }*/

        recycler.adapter = adapter

        dogListViewModel.dogList.observe(this) { dogList ->
            adapter.submitList(dogList)
        }

        //escuchador para que maneje el progress si no hay conexion a internet
        //esto más adelante se cambia por Sealed Class que es más avanzado y reemplaza los enum
        dogListViewModel.status.observe(this) { status ->

            when (status) {
                is ApiResponseStatus.Error -> {
                    //ocultar el progress bar
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_LONG)
                        .show()
                }
                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> loadingWheel.visibility = View.GONE
            }
            /*when (status) {
                ApiResponseStatus.LOADING -> {
                    //Mostrar Progressbar
                    loadingWheel.visibility = View.VISIBLE
                }
                ApiResponseStatus.ERROR -> {
                    //ocultar el progress bar
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, "Hubo un error al descargar los datos", Toast.LENGTH_LONG)
                        .show()
                }
                ApiResponseStatus.SUCCESS -> {
                    //ocultar progressbar
                    loadingWheel.visibility = View.GONE
                }
                else -> {
                    //ocultar progressbar
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, "Status desconocido", Toast.LENGTH_LONG)
                        .show()
                }
        }*//*se hizo uso en este caso con los enum*/
        }*/
    }

    private fun openDogDetailActivity(dog: Dog) {
        //pasar el dog a DogDetail, para esto se usa el plugin id 'kotlin-parcelize'
        // y se debe parcerlizar el objeto Dog
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        startActivity(intent)
    }
}