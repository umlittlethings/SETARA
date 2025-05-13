package com.chrisp.setaraapp.feature.onboarding

import android.content.Context
import android.content.SharedPreferences

object OnboardingPreferences {
    private const val PREF_NAME = "onboarding_prefs"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    fun isOnboardingCompleted(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
}