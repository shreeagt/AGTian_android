package com.agt.videostream.ui.screen.dashboard

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.agt.videostream.R
import com.agt.videostream.data.VideoData
import com.agt.videostream.ui.navigation.Screen
import kotlinx.coroutines.launch


@Composable
fun DashBoardScreen(
    navController: NavController,
    viewModel: DashBoardViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val allVideo by viewModel.videoList.collectAsState()
    val approveVideo by viewModel.approveVideo.collectAsState()
    val rejectedVideo by viewModel.rejectVideo.collectAsState()
    val sortlistVideo by viewModel.shortList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isApproveExtend by remember { mutableStateOf(false) }
    var isRejectExtend by remember { mutableStateOf(false) }
    var isSortListExpand by remember { mutableStateOf(false) }
    val totalApprove by viewModel.totalApproveVideo.collectAsState()

    LaunchedEffect(key1 = true) {
        launch {
            viewModel.message.collect {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    DisposableEffect(key1 = lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getAllVideo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .zIndex(1000f),
                strokeWidth = 3.dp,
                color = Color(0xFF281455)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF281455))
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ajanta_png),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(percent = 100))
                        .background(Color.White),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Welcome",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(10.dp)
                )

            }


            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp),
            ) {

                Spacer(modifier = Modifier.height(10.dp))
                Card(modifier = Modifier.clickable {
                    isApproveExtend = isApproveExtend.not()
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_approve),
                            contentDescription = "shortlist",
                            modifier = Modifier.size(48.dp)

                        )
                        Text(
                            text = "Total Approved Videos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = approveVideo.size.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    AnimatedVisibility(visible = isApproveExtend) {
                        LazyColumn {
                            items(approveVideo) {
                                VideoItem(video = it, "Approve") {
                                    viewModel.setMessage("You can't play videos once you approve")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(modifier = Modifier.clickable {
                    isRejectExtend = isRejectExtend.not()
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_reject),
                            contentDescription = "reject",
                            modifier = Modifier.size(48.dp)

                        )
                        Text(
                            text = "Total Rejected Videos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = rejectedVideo.size.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    AnimatedVisibility(visible = isRejectExtend) {
                        LazyColumn {
                            items(rejectedVideo) {
                                VideoItem(video = it, "Reject") {
                                    viewModel.setMessage("You can't play videos once you reject")
                                }
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Card(modifier = Modifier.clickable {
                    isSortListExpand = isSortListExpand.not()
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_shortlist),
                            contentDescription = "shortlist",
                            modifier = Modifier.size(48.dp)

                        )
                        Text(
                            text = "Total Shortlisted Videos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = sortlistVideo.size.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    AnimatedVisibility(visible = isSortListExpand) {
                        LazyColumn {
                            items(sortlistVideo) {
                                VideoItem(video = it, "Shortlist") { video ->
                                    if (totalApprove == 2) {
                                        viewModel.setMessage("You have already approve 2 videos")
                                    } else {
                                        navController.navigate(
                                            Screen.VideoScreen.withArgs(
                                                video.id,
                                                true
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "Video List", style = MaterialTheme.typography.headlineSmall)
                LazyColumn {
                    item {
                        if (allVideo.isEmpty()) {
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "You have finish all the video !",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(allVideo) { video ->
                        VideoItem(video = video, "Pending") {
                            navController.navigate(Screen.VideoScreen.withArgs(video.id, false))
                        }
                    }
                }


            }


        }
    }


}


@Composable
fun VideoItem(video: VideoData, statusMsg: String, onClick: (VideoData) -> Unit = {}) {

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
        val (thumbnail, city, title, status) =
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
            text = statusMsg,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
                start.linkTo(title.end, 16.dp)
            },
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = when (statusMsg) {
                "Reject" -> Color(0xFFC90707)
                "Approve" -> Color(0xFF009614)
                "Shortlist" -> Color(0xFFFDBE01)
                else -> Color(0xFF460BB1)
            }
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