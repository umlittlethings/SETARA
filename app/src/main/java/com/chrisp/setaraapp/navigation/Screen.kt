package com.chrisp.setaraapp.navigation

sealed class Screen(val route: String){
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Sekerja : Screen("sekerja")
    object Sertifikat : Screen("sertifikat")
    object Profile : Screen("profile")
    object Cvfeature : Screen("cv_feature")
    object DetailProgram : Screen("detail_Program")
    object DetailTugas : Screen("detail_tugas")
}