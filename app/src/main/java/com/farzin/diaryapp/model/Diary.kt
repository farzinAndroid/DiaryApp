package com.farzin.diaryapp.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class Diary : RealmObject {

    @PrimaryKey
    var _id : ObjectId = ObjectId.create()
    var ownerId :String = ""
    var title:String = ""
    var description:String = ""
    var date : RealmInstant = RealmInstant.from(System.currentTimeMillis(),0)
    var image : RealmList<String> = realmListOf()
    var mood : String = Mood.Neutral.name

}