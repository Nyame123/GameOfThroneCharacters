package com.bismark.gameofthronecharacters.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bismark.gameofthronecharacters.R
import com.bismark.gameofthronecharacters.ui.theme.MovieCharacterViewModel
import com.bismark.gameofthronecharacters.ui.widgets.ImageItemRow

@Composable
fun CharacterDetailScreen(viewModel: MovieCharacterViewModel) {
    val movieCharacter = viewModel.selectedCharacter.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .align(alignment = Alignment.CenterHorizontally),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_account_circle_24),
            contentDescription = "user profile"
        )
        Spacer(modifier = Modifier.height(8.dp))
        ImageItemRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            itemLabel = "Name", labelValue = movieCharacter.name!!.ifEmpty { movieCharacter.alias }
        )

        Spacer(modifier = Modifier.height(8.dp))
        ImageItemRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            itemLabel = "Culture", labelValue = movieCharacter.culture
        )
        Spacer(modifier = Modifier.height(8.dp))

        ImageItemRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            itemLabel = "Gender", labelValue = movieCharacter.gender
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
