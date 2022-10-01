package com.bismark.gameofthronecharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bismark.gameofthronecharacters.data_layer.paging.RemotePresentationState
import com.bismark.gameofthronecharacters.data_layer.paging.asRemotePresentationState
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.ui.theme.GameOfThroneCharactersTheme
import com.bismark.gameofthronecharacters.ui.theme.MovieCharacterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MovieCharacterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameOfThroneCharactersTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MovieCharacterList(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MovieCharacterList(viewModel: MovieCharacterViewModel) {
    val movieCharacters = viewModel.movieCharacterPagerFlow.collectAsLazyPagingItems()
    var currentLoadState = remember {
        RemotePresentationState.INITIAL
    }

    LazyColumn {
        items(movieCharacters) { movieCharacter ->
            movieCharacter?.let {
                MovieItemCard(movieCharacter = it)
            }
        }
        when (val state = movieCharacters.loadState.asRemotePresentationState(currentLoadState)) {
            RemotePresentationState.REMOTE_LOADING -> {
                currentLoadState = state
                item {
                    LoadingProgressBar()
                }
            }

            RemotePresentationState.SOURCE_LOADING -> {
                currentLoadState = state
                item {
                    LoadingProgressBar()
                }
            }

            RemotePresentationState.ERROR -> {
                currentLoadState = state
                item {
                    ErrorItem("Error loading the next page")
                }
            }

            else -> {}
        }
    }
}

@Composable
fun MovieItemCard(movieCharacter: MovieCharacter) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(8.dp)
                .clickable { }
        ) {
            Text(
                text = movieCharacter.name!!.ifEmpty { movieCharacter.alias },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movieCharacter.culture,
                color = colorResource(id = R.color.black),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ErrorItem(message: String) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp),
                painter = painterResource(id = R.mipmap.ic_error),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                color = Color.White,
                text = message,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(CenterVertically)
            )
        }
    }
}

@Composable
fun LoadingProgressBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White)
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameOfThroneCharactersTheme {
        LoadingProgressBar()
    }
}
