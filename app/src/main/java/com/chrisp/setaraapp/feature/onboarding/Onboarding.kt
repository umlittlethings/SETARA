package com.chrisp.setaraapp.feature.onboarding

import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import com.chrisp.setaraapp.R

data class OnboardingModel(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    companion object {
        val FirstPage = OnboardingModel(
            image = R.drawable.intro1,
            title = R.string.onBoardingTitle1,
            description = R.string.onBoardingText1
        )
        val SecondPage = OnboardingModel(
            image = R.drawable.intro2,
            title = R.string.onBoardingTitle2,
            description = R.string.onBoardingText2
        )
        val ThirdPage = OnboardingModel(
            image = R.drawable.intro3,
            title = R.string.onBoardingTitle3,
            description = R.string.onBoardingText3
        )
    }
}