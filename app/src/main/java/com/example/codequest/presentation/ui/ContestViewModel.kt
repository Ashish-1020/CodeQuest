package com.example.codequest.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codequest.data.Contest

import com.example.codequest.di.ContestRepository
import com.example.codequest.di.RoomContestRepository
import com.example.codequest.room.data.ContestReminder
import com.example.codequest.util.Constants.API_KEY
import com.example.codequest.util.Constants.USER_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContestViewModel @Inject constructor(
    private val repository: ContestRepository,
    private val repo:RoomContestRepository
) : ViewModel() {

    private val _contests = MutableLiveData<List<Contest>>()
    val contests: LiveData<List<Contest>> get() = _contests



    // New LiveData for stored contests
    private val _contestsStored = MutableLiveData<List<ContestReminder>>()
    val contestsStored: LiveData<List<ContestReminder>> get() = _contestsStored
    init {
        fetchContests()
        fetchContestsStored()
    }

    private fun fetchContests() {
        viewModelScope.launch {
            try {
                val response = repository.getContests(USER_NAME, API_KEY)
                _contests.value = response.objects
            } catch (e: Exception) {
                Log.e("ContestViewModel", "Error: ${e.message}")
            }
        }
    }






    fun insertContest(contest: ContestReminder) {
        viewModelScope.launch {
            repo.insertContest(contest)
        }
    }

    // Modified fetchContestsStored
    fun fetchContestsStored() {
        viewModelScope.launch {
            try {
                val contests = repo.getAllContests()
                _contestsStored.value = contests
            } catch (e: Exception) {
                Log.e("ContestViewModel", "Error fetching stored contests: ${e.message}")
            }
        }
    }
    fun deleteContest(contestId: Int) {
        viewModelScope.launch {
            repo.deleteContest(contestId)
        }
    }



}
