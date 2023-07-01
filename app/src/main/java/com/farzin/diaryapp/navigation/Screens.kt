package com.farzin.diaryapp.navigation

import com.farzin.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT

sealed class Screens(val route: String) {

    object Auth : Screens(route = "auth_screen")
    object Home : Screens(route = "home_screen")
    object Write : Screens(route = "write_screen?$WRITE_SCREEN_ARGUMENT={$WRITE_SCREEN_ARGUMENT}") {
        fun passDiaryId(diaryId: String) =
            "write_screen?$WRITE_SCREEN_ARGUMENT=$diaryId"
    }

}
