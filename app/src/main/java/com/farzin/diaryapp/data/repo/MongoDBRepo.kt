package com.farzin.diaryapp.data.repo

import com.farzin.diaryapp.model.Diary
import com.farzin.diaryapp.model.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


typealias Diaries = RequestState<Map<LocalDate,List<Diary>>>
interface MongoDBRepo {

    fun configureTheRealm()

    fun getAllDiaries() : Flow<Diaries>


}