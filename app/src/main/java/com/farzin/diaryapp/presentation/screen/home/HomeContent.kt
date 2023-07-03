package com.farzin.diaryapp.presentation.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farzin.diaryapp.model.Diary
import com.farzin.diaryapp.presentation.components.DiaryHolder
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    homeDiaries: Map<LocalDate, List<Diary>>,
    onClick: (String) -> Unit,
) {

    if (homeDiaries.isNotEmpty()) {

        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {

            homeDiaries.forEach { (localDate, diaries) ->
                stickyHeader(key = localDate) {
                    DateHeader(localDate = localDate)
                }

                items(
                    diaries,
                    key = { it._id }
                ) {
                    DiaryHolder(diary = it, onClick = onClick)
                }

            }

        }

    } else {
        EmptyPageScreen()
    }

}