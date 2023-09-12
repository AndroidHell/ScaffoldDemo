package com.example.diceweightcalculator

sealed class Screens(val route : String) {
    object Home : Screens("home_screen")
    object Sessions : Screens("sessions_screen")
    object Settings : Screens("settings_screen")
    object Donate : Screens("donate_screen")
    object NewSession : Screens("new_session_screen")
}