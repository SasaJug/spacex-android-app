package com.sasaj.spacexapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import kotlinx.coroutines.channels.Channel

class ListFragment : Fragment() {

    private lateinit var launches: MutableList<LaunchListQuery.Launch?>
    private val viewModel: ListViewModel by viewModels()

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
                    Launches()
                }
            }
        }
        return view
    }

    @Composable
    fun Launches() {
        val launchesState = viewModel.list.observeAsState(initial = emptyList())
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            val painter = rememberImagePainter(
                launch.links?.mission_patch_small ?: "",
                builder = {})
            Image(
                painter, "", modifier = Modifier
                    .size(100.dp)
            )
            Column {
                Text(launch.mission_name ?: "")
                Text(launch.rocket?.rocket?.name ?: "")
            }
        }
    }

    @Preview(showBackground = true, name = "Text preview")
    @Composable
    fun DefaultPreview() {
        Surface(color = MaterialTheme.colors.background) {
            LaunchesList(listOf(LaunchListQuery.Launch("", null, null, "Mission 1")))
        }
    }
}