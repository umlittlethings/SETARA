package com.chrisp.setaraapp.onboarding.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chrisp.setaraapp.onboarding.data.OnboardingModel

class OnboardingViewModel : ViewModel() {
    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    private val _onboardingItems = listOf(
        OnboardingModel.FirstPage,
        OnboardingModel.SecondPage,
        OnboardingModel.ThirdPage
    )

    val onboardingItems: List<OnboardingModel> = _onboardingItems

    fun nextPage() {
        if (_currentIndex.value < _onboardingItems.size - 1) {
            _currentIndex.value += 1
        }
    }

    fun previousPage() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
        }
    }

    fun lastPage() {
        _currentIndex.value = _onboardingItems.size - 1
    }
}

