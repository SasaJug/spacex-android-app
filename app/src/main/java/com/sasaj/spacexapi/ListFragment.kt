package com.sasaj.spacexapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
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
   
                }
            }
        }
        return view
    }


    @Composable
    private fun LaunchesList(launches: List<LaunchListQuery.Launch?>){
//        val launchesState = viewModel.list.observeAsState()
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
            items(items = launches){ launch->
                launch?.let {
                    LaunchRow(launch)
                }
            }
        }
    }

    @Composable
    private fun LaunchRow(launch: LaunchListQuery.Launch) {
        Surface(color = MaterialTheme.colors.primary) {
            Text (text = "${launch.mission_name}", modifier = Modifier.padding(8.dp))
        }
    }

    @Preview(showBackground = true, name = "Text preview")
    @Composable
    fun DefaultPreview() {
        Surface(color = MaterialTheme.colors.background) {
            LaunchesList(listOf(LaunchListQuery.Launch("",null,null, "Mission 1")))
        }
    }
}