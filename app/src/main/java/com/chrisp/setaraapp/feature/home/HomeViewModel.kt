package com.chrisp.setaraapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.auth.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _userName = MutableStateFlow<Result<String>?>(null)
    val userName: StateFlow<Result<String>?> = _userName


    fun loadUserName() {
        viewModelScope.launch {
        }
    }
}