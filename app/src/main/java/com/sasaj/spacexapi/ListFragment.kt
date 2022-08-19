package com.sasaj.spacexapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.channels.Channel

class ListFragment : Fragment() {

    private lateinit var launches: MutableList<LaunchListQuery.Launch?>
    private val viewModel: ListViewModel by viewModels()

    private val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launches = mutableListOf()

        val channel = Channel<Unit>(Channel.CONFLATED)

        channel.trySend(Unit)
        channel.close()

        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                Surface(color = MaterialTheme.colors.background) {
                    MainContent()
                }
            }
        }
        return view
    }

    @Composable
    fun MainContent() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "SpaceX",
                            color = Color.White
                        )
                    },
                )
            },
            content = { Launches() }
        )
    }

    @Composable
    fun Launches() {
        val launchesState = viewModel.list.observeAsState(initial = emptyList())
        progress(isDisplayed = viewModel.loading.value)
        LaunchesList(launches = launchesState.value)
    }


    @Composable
    private fun LaunchesList(launches: List<LaunchListQuery.Launch?>) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = launches) { launch ->
                launch?.let {
                    LaunchRow(launch)
                }
            }
        }
    }

    @Composable
    private fun LaunchRow(launch: LaunchListQuery.Launch) {
        Surface(modifier = Modifier.clickable {
            val action = ListFragmentDirections.openLaunchDetails(launchId = launch.id ?: "")
            navController.navigate(action)
        })
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val painter = rememberImagePainter(
                    launch.links?.mission_patch_small ?: "",
                    builder = {})
                Image(
                    painter, "", modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                )
                Column {
                    Text(launch.mission_name ?: "")
                    Text(launch.rocket?.rocket?.name ?: "")
                }
            }
        }
    }

    @Composable
    private fun progress(isDisplayed: Boolean) {
        if (isDisplayed) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 100.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    @Preview(showBackground = true, name = "Text preview")
    @Composable
    fun DefaultPreview() {
        Surface(color = MaterialTheme.colors.background) {
            LaunchesList(
                listOf(
                    LaunchListQuery.Launch(
                        "",
                        null,
                        null,
                        null,
                    )
                )
            )
        }
    }
}