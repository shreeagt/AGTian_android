package com.agt.videostream.ui.screen.shortlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.agt.videostream.R
import com.agt.videostream.data.VideoData
import com.agt.videostream.ui.navigation.Screen

@Composable
fun ShortListScreen(
    navController : NavController,
    viewModel: ViewModel = hiltViewModel()
){

    val context = LocalContext.current


}

@Composable
fun VideoItem(video: VideoData, onClick: (VideoData) -> Unit = {}) {

    ConstraintLayout(
        modifier =
        Modifier
            .testTag("VideoParent")
            .padding(8.dp)
            .wrapContentSize()
            .clickable {
                onClick(video)
            }
    ) {
        val (thumbnail, city, title) =
            createRefs()

        // thumbnail
        Box(modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(elevation = 20.dp)
            .constrainAs(thumbnail) {
                top.linkTo(
                    parent.top,
                    margin = 8.dp
                )
                start.linkTo(
                    parent.start,
                    margin = 8.dp
                )
                bottom.linkTo(parent.bottom)
            }) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Thumbnail",
                modifier =
                Modifier
                    .size(64.dp)

            )
            Image(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Play",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }

        // title
        Text(
            text = video.name,
            modifier =
            Modifier.constrainAs(title) {
                top.linkTo(thumbnail.top, margin = 8.dp)
                start.linkTo(
                    thumbnail.end,
                    margin = 16.dp
                )
                width = Dimension.preferredWrapContent
                height = Dimension.wrapContent
            },
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            softWrap = true,
        )
        Text(
            text = video.city,
            modifier =
            Modifier.constrainAs(city) {
                top.linkTo(title.bottom, margin = 8.dp)
                start.linkTo(
                    title.start,
                )

                width = Dimension.preferredWrapContent
                height = Dimension.wrapContent
            },
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            softWrap = true,
        )

        // divider
        Divider(
            modifier =
            Modifier
                .padding(horizontal = 8.dp)
                .testTag("Divider"),
            color = Color(0xFFE0E0E0)
        )

    }
}