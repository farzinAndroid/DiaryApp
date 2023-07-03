package com.farzin.diaryapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farzin.diaryapp.R
import com.farzin.diaryapp.data.repo.MongoDB
import com.farzin.diaryapp.presentation.components.DisplayAlertDialog
import com.farzin.diaryapp.presentation.screen.auth.AuthScreen
import com.farzin.diaryapp.presentation.screen.home.HomeScreen
import com.farzin.diaryapp.util.Constants.APP_ID
import com.farzin.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT
import com.farzin.diaryapp.viewmodels.AuthViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        authScreen() {
            navController.popBackStack()
            navController.navigate(Screens.Home.route)
        }
        homeScreen(
            navigateToWriteScreen = {
                navController.navigate(Screens.Write.route)
            },
            navigateToAuthScreen = {
                navController.popBackStack()
                navController.navigate(Screens.Auth.route)
            }
        )
        writeScreen()

    }

}

@ExperimentalMaterial3Api
fun NavGraphBuilder.authScreen(
    navigateToHome: () -> Unit,
) {
    composable(route = Screens.Auth.route) {

        val vm: AuthViewModel = viewModel()

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val loadingState by vm.loading
        val isAuthenticated by vm.isAuthenticated

        AuthScreen(
            loadingState = loadingState,
            isAuthenticated = isAuthenticated,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onClick = {
                oneTapState.open()
                vm.setLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                vm.signInToMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {

                        messageBarState.addSuccess("Successfully Authenticated")
//                            Log.e("TAG", "authScreen: token:  $tokenId", )

                        vm.setLoading(false)
                    },
                    onError = { error ->
                        messageBarState.addError(error)
                        vm.setLoading(false)
                    }
                )

            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                vm.setLoading(false)
            },
            navigateToHome = {
                navigateToHome()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreen(
    navigateToWriteScreen: () -> Unit,
    navigateToAuthScreen: () -> Unit,
) {
    composable(route = Screens.Home.route) {

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        var dialogOpened by remember { mutableStateOf(false) }

        HomeScreen(
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWriteScreen = navigateToWriteScreen,
            state = drawerState,
            onSignOutClicked = { dialogOpened = true }
        )

        LaunchedEffect(Unit){
            MongoDB.configureTheRealm()
        }

        DisplayAlertDialog(
            title =stringResource(R.string.dialog_title),
            message = stringResource(R.string.dialog_message),
            dialogOpened =dialogOpened,
            onCloseDialog = { dialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null){
                        user.logOut()
                        withContext(Dispatchers.Main){
                            navigateToAuthScreen()
                        }
                    }
                }

            }
        )
    }
}


fun NavGraphBuilder.writeScreen() {
    composable(
        route = Screens.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT) {
            nullable = true
            type = NavType.StringType
            defaultValue = null
        })
    ) {

    }
}