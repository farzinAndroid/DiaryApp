package com.farzin.diaryapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzin.diaryapp.data.repo.Diaries
import com.farzin.diaryapp.data.repo.MongoDB
import com.farzin.diaryapp.model.RequestState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf<Diaries>(RequestState.Idle)

    init {
        getDiaries()
    }

    private fun getDiaries() {
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }


}