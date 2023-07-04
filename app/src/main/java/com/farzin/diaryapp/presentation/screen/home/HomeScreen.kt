package com.farzin.diaryapp.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farzin.diaryapp.R
import com.farzin.diaryapp.data.repo.Diaries
import com.farzin.diaryapp.model.RequestState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    diaries: Diaries,
    onMenuClicked:()->Unit,
    navigateToWriteScreen:()->Unit,
    onSignOutClicked: () -> Unit,
    state: DrawerState,
) {

    NavDrawer(
        state =state,
        onSignOutClicked =onSignOutClicked
    ) {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
                HomeTopBar(
                    onMenuClicked =onMenuClicked
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = navigateToWriteScreen) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription =""
                    )
                }
            },
            content = {
//                HomeContent(homeDiaries = mapOf(), onClick = {})
                when(diaries){
                    is RequestState.Success->{
                        HomeContent(homeDiaries = diaries.data, onClick = {})
                    }
                    is RequestState.Error->{
                        EmptyPageScreen(
                            title = stringResource(R.string.error),
                            subTitle = "${diaries.error.message}"
                        )
                    }
                    is RequestState.Loading->{
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    else->{
                        Text(text = "Error")
                    }
                }


            }
        )
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(
    state: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit,
) {


    ModalNavigationDrawer(
        drawerState = state,
        drawerContent = {

               ModalDrawerSheet(
                   content={
                       Box(modifier = Modifier
                           .height(250.dp)
                           .fillMaxWidth()
                           ,
                           contentAlignment = Alignment.Center
                       ){

                           Image(
                               painter = painterResource(R.drawable.logo),
                               contentDescription = "",
                               modifier = Modifier
                                   .size(250.dp)
                           )
                       }


                       NavigationDrawerItem(
                           label = {

                               Row(
                                   modifier = Modifier.padding(horizontal = 12.dp)
                               ) {

                                   Icon(
                                       painter = painterResource(R.drawable.google_logo),
                                       contentDescription = "",
                                       tint = MaterialTheme.colorScheme.onSurface
                                   )

                                   Spacer(modifier = Modifier.width(12.dp))

                                   Text(
                                       text = stringResource(R.string.sign_out),
                                       color = MaterialTheme.colorScheme.onSurface
                                   )

                               }


                           },
                           selected = false,
                           onClick = onSignOutClicked
                       )
                   }
               )




        },
        content = content,
    )


}