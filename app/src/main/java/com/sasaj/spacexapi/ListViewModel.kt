package com.sasaj.spacexapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apolloClient
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.launch


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
