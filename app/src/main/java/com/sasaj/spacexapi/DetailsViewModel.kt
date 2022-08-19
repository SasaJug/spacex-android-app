package com.sasaj.spacexapi

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apolloClient
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val _details = MutableLiveData<LaunchDetailsQuery.Launch>()
    val details: LiveData<LaunchDetailsQuery.Launch>
        get() = _details

    fun getLaunchDetails(id: String) {
        viewModelScope.launch {
            val response = try {
                apolloClient.query(LaunchDetailsQuery(id)).execute()
            } catch (e: ApolloException) {
                Log.d("LaunchList", "Failure", e)
                return@launch
            }
            response.data?.launch?.let {
                _details.value = it
            }

        }
    }
}


