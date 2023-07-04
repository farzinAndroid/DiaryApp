package com.farzin.diaryapp.data.repo

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.farzin.diaryapp.R
import com.farzin.diaryapp.model.Diary
import com.farzin.diaryapp.model.RequestState
import com.farzin.diaryapp.util.Constants.APP_ID
import com.farzin.diaryapp.util.Constants.SUB_NAME
import com.farzin.diaryapp.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId

object MongoDB : MongoDBRepo {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private     lateinit var realm: Realm


    init {
        configureTheRealm()
    }

    //this method is for syncing the data between user and Mogo DB realm
    override fun configureTheRealm() {
        if (user != null) {
            // we need config to sync the data
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))//sync this class
                .initialSubscriptions { sub -> // filter only the correct result from data base

                    // add new sub to our sub set
                    add(
                        //in this query we use ownerId to access those diaries that were created by ourselves
                        query = sub.query<Diary>("ownerId == $0", user.identity,),
                        // here we are subscribing to all diaries that contain the ownerId of our current authenticated user
                        name = SUB_NAME
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }

    }

    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user.identity)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }
}

private class UserNotAuthenticatedException() : Exception("user is not logged in")