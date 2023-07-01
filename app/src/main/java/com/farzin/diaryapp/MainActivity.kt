package com.farzin.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.farzin.diaryapp.navigation.NavGraph
import com.farzin.diaryapp.navigation.Screens
import com.farzin.diaryapp.ui.theme.DiaryAppTheme
import com.farzin.diaryapp.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryAppTheme {
                val navController = rememberNavController()

                NavGraph(
                    navController = navController,
                    startDestination = getStartDestination()
                )
            }
        }
    }

    private fun getStartDestination(): String {
        val user = App.create(APP_ID).currentUser
        return if (user != null && user.loggedIn) Screens.Home.route
        else Screens.Auth.route
    }

}