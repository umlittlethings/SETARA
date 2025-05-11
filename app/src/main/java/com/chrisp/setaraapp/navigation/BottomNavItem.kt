package com.chrisp.setaraapp.navigation

import androidx.annotation.DrawableRes
import com.chrisp.setaraapp.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        title = "Home",
        icon = R.drawable.ic_home
    )
    object Sekerja : BottomNavItem(
        route = Screen.Sekerja.route,
        title = "Sekerja",
        icon = R.drawable.ic_sekerja
    )
    object Sertifikat : BottomNavItem(
        route = Screen.Sertifikat.route,
        title = "Sertifikat",
        icon = R.drawable.ic_sertifikat
    )
    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        title = "Profil",
        icon = R.drawable.ic_profile
    )

}