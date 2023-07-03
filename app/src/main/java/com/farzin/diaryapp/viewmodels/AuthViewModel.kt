package com.farzin.diaryapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzin.diaryapp.R
import com.farzin.diaryapp.util.Constants.APP_ID
import com.farzin.diaryapp.util.Constants.toString
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthViewModel() : ViewModel() {

    var isAuthenticated = mutableStateOf(false)

    var loading = mutableStateOf(false)


    fun setLoading(loadingBool: Boolean) {
        loading.value = loadingBool
    }

    fun signInToMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {

        viewModelScope.launch {
            try {

                val result = withContext(Dispatchers.IO){

                    App.create(APP_ID).login(
                        /*Credentials.google(
                            token = tokenId,
                            type = GoogleAuthType.ID_TOKEN
                        )*/
                    Credentials.jwt(tokenId)
                    ).loggedIn
                }

                withContext(Dispatchers.Main){
                    if (result){
                        onSuccess()
                        delay(1000)
                        isAuthenticated.value = true
                    }else{
                        onError(Exception("user is not logged in"))
                    }

                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    onError(e)
                }
            }
        }

    }

}