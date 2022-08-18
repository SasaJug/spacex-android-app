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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import kotlinx.coroutines.channels.Channel
import org.intellij.lang.annotations.JdkConstants

class LaunchDetailsFragment : Fragment() {
    private val viewModel: DetailsViewModel by viewModels()

    private val navController : NavController by lazy { findNavController() }
    val args: LaunchDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val channel = Channel<Unit>(Channel.CONFLATED)

        channel.trySend(Unit)
        channel.close()
        viewModel.getLaunchDetails(args.launchId)
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
    fun MainContent(){
        val launchDetails = viewModel.details.observeAsState(initial =LaunchDetailsQuery.Launch(null, null, null, null, null, null)).value
        Scaffold(
            topBar = {
                TopAppBar(
                title = {
                    Text(
                    launchDetails.mission_name ?: "",
                    color = Color.White)
                },
                    navigationIcon = if (navController.previousBackStackEntry != null) {
                        {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    } else {
                        null
                    }
            ) },
            content = { LaunchDetails(launch = launchDetails)}
        )
    }

    @Composable
    private fun LaunchDetails(launch: LaunchDetailsQuery.Launch) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.Start) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = rememberImagePainter(launch.links?.mission_patch),
                    contentDescription = "Mission patch",
                )
            }
            title(text = "Mission details")
            paragraph(text = launch.details ?: "")
        }
    }
}

@Composable
private fun title(text: String) {
    Text(text, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
}

@Composable
private fun paragraph(text: String) {
    Text(text, fontWeight = FontWeight.Normal, fontSize = 16.sp)
}


