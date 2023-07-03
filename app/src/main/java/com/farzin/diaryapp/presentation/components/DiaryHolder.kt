package com.farzin.diaryapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farzin.diaryapp.R
import com.farzin.diaryapp.model.Diary
import com.farzin.diaryapp.model.Mood
import com.farzin.diaryapp.presentation.screen.home.DateHeader
import com.farzin.diaryapp.ui.theme.Elevation
import com.farzin.diaryapp.util.toInstant
import io.realm.kotlin.ext.realmListOf
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Composable
fun DiaryHolder(
    diary: Diary,
    onClick: (String) -> Unit,
) {

    var componentHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    var showGallery by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(diary._id.toString())
            }
    ) {

        Spacer(modifier = Modifier.width(14.dp))

        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level5
        ) {}

        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.Level1
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DiaryHeader(
                    moodName = diary.mood,
                    time = diary.date.toInstant()
                )

                Text(
                    text = diary.description,
                    modifier = Modifier
                        .padding(14.dp),
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (diary.image.isNotEmpty()){
                    ShowGalleryButton(
                        showGallery = showGallery,
                        onClick = {
                            showGallery = !showGallery
                        }
                    )
                }

                AnimatedVisibility(visible = showGallery) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Gallery(images = diary.image)
                    }

                }


            }

        }

    }

}


@Composable
fun ShowGalleryButton(
    showGallery:Boolean,
    onClick:()->Unit
) {
    TextButton(onClick = { onClick() }) {
        Text(
            text = if (showGallery) stringResource(R.string.hide_gallery) else stringResource(R.string.show_gallery),
            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
        )
    }
}

@Composable
fun DiaryHeader(
    moodName: String, time: Instant,
) {

    val mood by remember {
        mutableStateOf(Mood.valueOf(moodName))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(mood.icon),
                contentDescription = "",
                modifier = Modifier
                    .size(18.dp)
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }

        Text(
            text = SimpleDateFormat("hh:mm a", Locale.US)
                .format(Date.from(time)),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }


}


@Preview(showBackground = true)
@Composable
fun DiaryHolderPreview() {
    DiaryHolder(
        diary = Diary().apply {
            title = "Diary"
            description =
                "sjdghsgchjgdshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhc"
            mood = Mood.Happy.name
            image = realmListOf("","","")
        },
        onClick = {}
    )
}