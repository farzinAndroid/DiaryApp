package com.farzin.diaryapp.presentation.screen.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.farzin.diaryapp.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun AuthScreen(
    isAuthenticated:Boolean,
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    messageBarState : MessageBarState,
    onClick: () -> Unit,
    onTokenIdReceived:(String)->Unit,
    onDialogDismissed:(String)->Unit,
    navigateToHome:()->Unit,

) {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            ,
        content = {
            ContentWithMessageBar(
                messageBarState = messageBarState,
                errorMaxLines = 3,
                showToastOnCopy = true,
                visibilityDuration = 5000
            ) {
                AuthScreenContent(
                    loadingState = loadingState,
                    onClick = { onClick() }
                )
            }
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { token ->
            onTokenIdReceived(token)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        }
    )

    LaunchedEffect(isAuthenticated){
        if (isAuthenticated){
            navigateToHome()
        }
    }

}