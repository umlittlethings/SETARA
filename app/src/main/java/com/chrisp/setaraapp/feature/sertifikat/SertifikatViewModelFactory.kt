package com.chrisp.setaraapp.feature.sertifikat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chrisp.setaraapp.feature.auth.AuthViewModel

class SertifikatViewModelFactory(private val authViewModel: AuthViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SertifikatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SertifikatViewModel(authViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}