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
    object DetailProgram : Screen("detail_program/{courseId}") {
        fun createRoute(courseId: String) = "detail_program/$courseId"
    }
    object DetailTugas : Screen("detail_tugas")
    object CompleteProfile : Screen("complete_profile")
    object EnrollmentSuccess : Screen("enrollment_success")
    object Notification : Screen("notification")
    object ChangePassword : Screen("change_password")
    // Base route
    fun DetailTugasRoute(courseId: String, assignmentId: String) = "$DetailTugas/$courseId/$assignmentId"
}