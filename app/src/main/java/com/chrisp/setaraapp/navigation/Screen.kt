package com.chrisp.setaraapp.navigation

sealed class Screen(val route: String){
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Sekerja : Screen("sekerja")
    object Sertifikat : Screen("sertifikat")
    object Profile : Screen("profile")
}