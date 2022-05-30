package com.sasaj.spacexapi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import apolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.sasaj.spacexapi.databinding.LaunchListFragmentBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class LaunchListFragment : Fragment() {
    private lateinit var adapter: LaunchListAdapter
    private lateinit var launches: MutableList<LaunchListQuery.Launch?>
    private val viewModel: ListViewModel by viewModels()
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

        launches = mutableListOf()
        adapter = LaunchListAdapter(launches)
        binding.launches.layoutManager = LinearLayoutManager(requireContext())
        binding.launches.adapter = adapter

        val channel = Channel<Unit>(Channel.CONFLATED)

        channel.trySend(Unit)
        adapter.onEndOfListReached = {
            channel.trySend(Unit)
        }

        adapter.onEndOfListReached = null
        channel.close()

        observe()
    }

    private fun observe() {
        viewModel.list.observe(viewLifecycleOwner) {
            showList(it)
        }
    }

    private fun showList(list: List<LaunchListQuery.Launch?>) {
        launches.addAll(list as Collection<LaunchListQuery.Launch?>)
        adapter.notifyDataSetChanged()
    }
}

class ListViewModel : ViewModel() {

    private val _list = MutableLiveData<List<LaunchListQuery.Launch?>>()
    val list: LiveData<List<LaunchListQuery.Launch?>>
        get() = _list

    init {
        viewModelScope.launch {
            val response = try {
                apolloClient.query(LaunchListQuery()).execute()
            } catch (e: ApolloException) {
                Log.d("LaunchList", "Failure", e)
                return@launch
            }
            response.data?.launches?.let {
                _list.value = it
            }
        }
    }
}

