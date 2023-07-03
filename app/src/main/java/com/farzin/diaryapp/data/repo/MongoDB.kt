package com.farzin.diaryapp.data.repo

import com.farzin.diaryapp.model.Diary
import com.farzin.diaryapp.util.Constants.APP_ID
import com.farzin.diaryapp.util.Constants.SUB_NAME
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB : MongoDBRepo {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    lateinit var realm: Realm

    //this method is for syncing the data between user and Mogo DB realm
    override fun configureTheRealm() {
        if (user != null) {
            // we need config to sync the data
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))//sync this class
                .initialSubscriptions { sub -> // filter only the correct result from data base

                    // add new sub to our sub set
                    add(
                        //in this query we use ownerId to access those diaries that were created by ourselves
                        query = sub.query(
                            "ownerId == $0",
                            user.identity,
                        ), // here we are subscribing to all diaries that contain the ownerId of our current authenticated user
                        name = SUB_NAME
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }

    }
}