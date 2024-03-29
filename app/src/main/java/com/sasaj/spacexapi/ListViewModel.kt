package com.sasaj.spacexapi

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.sasaj.spacexapi.type.Launch
import kotlinx.coroutines.launch


class ListViewModel : ViewModel() {

    private val _list = MutableLiveData<List<LaunchListQuery.Launch?>>()
    val list: LiveData<List<LaunchListQuery.Launch?>>
        get() = _list

    val loading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            loading.value = true
            val response = try {
                apolloClient.query(LaunchListQuery()).execute()
            } catch (e: ApolloException) {
                Log.d("LaunchList", "Failure", e)
                return@launch
            }
            response.data?.launches?.let {
                loading.value = false
                _list.value = it
            }
        }
    }
}
