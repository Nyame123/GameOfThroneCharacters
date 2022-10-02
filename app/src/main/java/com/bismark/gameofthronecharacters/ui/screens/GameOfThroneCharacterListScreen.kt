package com.bismark.gameofthronecharacters.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bismark.gameofthronecharacters.R
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.ui.MovieCharacterViewModel

@Composable
fun MovieCharacterList(
    modifier: Modifier,
    viewModel: MovieCharacterViewModel,
    navHostController: NavHostController,
    onClick: (MovieCharacter) -> Unit
) {
    val movieCharacters = viewModel.movieCharacterPagerFlow.collectAsLazyPagingItems()

    LazyColumn(modifier = modifier) {
        items(movieCharacters) { movieCharacter ->
            movieCharacter?.let { mov ->
                MovieItemCard(movieCharacter = mov, onClick = {onClick(mov)})
            }
        }

        val state = movieCharacters.loadState
        when {
            state.refresh == LoadState.Loading -> {
                item {
                    LoadingProgressBar()
                }
            }

            state.append == LoadState.Loading -> {
                item {
                    LoadingProgressBar()
                }
            }

            state.refresh is LoadState.Error -> {
                item {
                    ErrorItem((state.refresh as LoadState.Error).error.message ?: "Error"){
                        movieCharacters.retry()
                    }
                }
            }

            state.append is LoadState.Error -> {
                item {
                    ErrorItem((state.refresh as LoadState.Error).error.message ?: "Error"){
                        movieCharacters.retry()
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun MovieItemCard(movieCharacter: MovieCharacter, onClick: (MovieCharacter) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(8.dp)
                .clickable { onClick(movieCharacter) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier.width(40.dp).height(40.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_account_circle_24),
                contentDescription = "user profile"
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = Color.White)
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
}

@Composable
fun ErrorItem(message: String, onClick: () -> Unit) {
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
                .clickable { onClick() }
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
                text =  "click to Retry. $message",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically)
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
