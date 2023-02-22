package com.jhonw.dogedex.doglist

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.jhonw.dogedex.GRID_SPAN_COUNT
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.composables.BackNavigationIcon
import com.jhonw.dogedex.composables.ErrorDialog
import com.jhonw.dogedex.composables.LoadingWheel
import com.jhonw.dogedex.model.Dog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalCoilApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun DogListScreen(
    //dogList: List<Dog>,//ya no son necesarios usando hilt, se llama mas abajo
    onDogClicked: (Dog) -> Unit,
    //status: ApiResponseStatus<Any>? = null,//ya no son necesarios usando hilt, se llama mas abajo
    onNavigationIconClick: () -> Unit,
    viewModel: DogListViewModel = hiltViewModel()
) {
    val status = viewModel.status.value
    val dogList = viewModel.dogList.value
    Scaffold(
        topBar = { DogListScreenTopBar(onNavigationIconClick) }
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 65.dp, bottom = 15.dp),
            columns = GridCells.Fixed(GRID_SPAN_COUNT),
            content = {
                items(dogList) {
                    DogGridItem(dog = it, onDogClicked)
                }
            }
        )
    }

    if (status is ApiResponseStatus.Loading) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error) {
        ErrorDialog(
            status.message
        ) { viewModel.resetApiResponseStatus() }
    }

    //le LazyColumn hace gran parte del adapter
    /*LazyColumn {
        items(dogList) {
            DogItem(dog = it, onDogClicked)
        }
    }*/
}

@Composable
fun DogListScreenTopBar(
    onClick: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.my_dog_collection)) },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        navigationIcon = { BackNavigationIcon(onClick) }
    )
}

@ExperimentalCoilApi
@ExperimentalMaterial3Api
@Composable
fun DogGridItem(
    dog: Dog,
    onDogClicked: (Dog) -> Unit
) {
    if (dog.inCollection) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            onClick = { onDogClicked(dog) },
            shape = RoundedCornerShape(4.dp)
        ) {
            Image(
                painter = rememberImagePainter(dog.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.White)
                    .semantics { testTag = "dog-${dog.name}" }
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            color = Color.Red,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = dog.index.toString(),
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun DogItem(
    dog: Dog,
    onDogClicked: (Dog) -> Unit
) {
    if (dog.inCollection) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onDogClicked(dog) },
            text = dog.name
        )
    } else {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.Red),
            text = stringResource(
                id = R.string.dog_index_format, dog.index
            )
        )
    }
}
