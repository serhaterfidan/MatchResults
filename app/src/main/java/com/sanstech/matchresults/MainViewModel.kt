package com.sanstech.matchresults

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private var _matchResponse = MutableLiveData<NetworkResult<List<Match>>>()
    val matchResponse: LiveData<NetworkResult<List<Match>>> = _matchResponse

    init {
        fetchMatchResults()
    }

    private fun fetchMatchResults() {
        viewModelScope.launch {
            mainRepository.getMatchResults().collect {
                _matchResponse.postValue(it)
            }
        }
    }
}