package com.bismark.gameofthronecharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bismark.gameofthronecharacters.ui.screens.CharacterDetailScreen
import com.bismark.gameofthronecharacters.ui.screens.LoadingProgressBar
import com.bismark.gameofthronecharacters.ui.screens.MovieCharacterList
import com.bismark.gameofthronecharacters.ui.screens.Routes
import com.bismark.gameofthronecharacters.ui.theme.GameOfThroneCharactersTheme
import com.bismark.gameofthronecharacters.ui.theme.MovieCharacterViewModel
import com.bismark.gameofthronecharacters.ui.widgets.TopBar
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
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MovieCharacterViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(title = "Game of Thrones Characters") }
    ) {
        MyAppNavHost(modifier = Modifier.padding(it), viewModel = viewModel)
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier,
    viewModel: MovieCharacterViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.CHARACTER_LIST_SCREEN, modifier = modifier) {
        composable(Routes.CHARACTER_LIST_SCREEN) {
            MovieCharacterList(
                modifier = Modifier.padding(),
                viewModel = viewModel,
                navHostController = navController,
                onClick = {
                    viewModel.selectCharacter(it)
                    navController.navigate(Routes.CHARACTER_DETAIL_SCREEN)
                }
            )
        }

        composable(Routes.CHARACTER_DETAIL_SCREEN){
            CharacterDetailScreen(viewModel = viewModel)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameOfThroneCharactersTheme {
        LoadingProgressBar()
    }
}
