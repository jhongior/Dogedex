package com.jhonw.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jhonw.dogedex.Dog
import com.jhonw.dogedex.GRID_SPAN_COUNT
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.databinding.ActivityDogListBinding
import com.jhonw.dogedex.dogdetail.DogDetailActivity
import com.jhonw.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY

class DogListActivity : AppCompatActivity() {

    private val dogListViewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingWheel = binding.loadingWheel

        val recycler = binding.dogRecycler
        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)

        val adapter = DogAdapter()
        adapter.setOnItemClickListener {
            //pasar el dog a DogDetail, para esto se usa el plugin id 'kotlin-parcelize'
            // y se debe parcerlizar el objeto Dog
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
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
        }
    }
}