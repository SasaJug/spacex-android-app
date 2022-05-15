package com.sasaj.spacexapi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import apolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.sasaj.spacexapi.databinding.LaunchListFragmentBinding
import kotlinx.coroutines.channels.Channel

class LaunchListFragment : Fragment() {
    private lateinit var binding: LaunchListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LaunchListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launches = mutableListOf<LaunchListQuery.Launch>()
        val adapter = LaunchListAdapter(launches)
        binding.launches.layoutManager = LinearLayoutManager(requireContext())
        binding.launches.adapter = adapter

        val channel = Channel<Unit>(Channel.CONFLATED)

        channel.trySend(Unit)
        adapter.onEndOfListReached = {
            channel.trySend(Unit)
        }
        lifecycleScope.launchWhenResumed {
            for (item in channel) {
                val response = try {
                    apolloClient.query(LaunchListQuery()).execute()
                } catch (e: ApolloException) {
                    Log.d("LaunchList", "Failure", e)
                    return@launchWhenResumed
                }
                response.data?.launches?.let {
                    launches.addAll(it as Collection<LaunchListQuery.Launch>)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        adapter.onEndOfListReached = null
        channel.close()
    }

}
