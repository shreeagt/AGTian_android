package com.agt.videostream.ui.screen.video

import android.view.ViewGroup
import android.widget.FrameLayout
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.agt.videostream.R
import com.agt.videostream.data.VideoData
import com.agt.videostream.ui.component.VideoDialouge
import com.agt.videostream.ui.theme.Purple40
import com.agt.videostream.ui.theme.Purple80
import com.agt.videostream.ui.theme.PurpleGrey80
import com.agt.videostream.ui.theme.deepGreen
import com.agt.videostream.util.ScreenState
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    navController: NavController,
    videoId: Int,
    isShortListed: Boolean,
    viewModel: VideoViewModel = hiltViewModel()
) {

    viewModel.onSetUP(videoId)
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    var dialougVisibility by remember { mutableStateOf(false) }
    val state by viewModel.screenState.collectAsState()
    val approveVideoCount by viewModel.totalApproveVideo.collectAsState()
    var alertMsg by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true) {
        viewModel.message.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(key1 = lifecycle) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }

    }



    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ScreenState.Error -> {

            }

            ScreenState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    strokeWidth = 3.dp
                )
            }

            is ScreenState.Success -> {
                val data = (state as ScreenState.Success<VideoData>).data
                Column(modifier = Modifier.fillMaxSize()) {

                    TopAppBar(
                        title = {
                            Image(
                                painter = painterResource(id = R.drawable.ajanta_png),
                                contentDescription = "logo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(percent = 100))
                                    .background(Color.White),
                                contentScale = ContentScale.Fit
                            )

                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )

                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors()
                    )

                    VideoPlayer(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(3f, fill = true)
                            .background(Color.White),
                        listOf(data),
                        onBackPress = {
                            navController.popBackStack()
                        }
                    )

                    BottomActions(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                            .padding(10.dp),
                        video = data,
                        isShortListed = isShortListed,
                        onApproveOrShortList = {
                            if (isShortListed) {
                                viewModel.onApprove(it)
                                if (approveVideoCount == 0) {
                                    alertMsg =
                                        "Approved! Your can approve one more video the rest will be rejected automatically . Thanks for your amazing contribution!"
                                } else if (approveVideoCount == 1) {
                                    alertMsg =
                                        "Approved! Your have approve two videos, rest is rejected automatically . Thanks for your amazing contribution!"
                                }
                                dialougVisibility = true
                            } else {
                                viewModel.shortList(it)
                                alertMsg =
                                    "Short List! Your video shines in AGTians. Thanks for your amazing contribution!"
                                dialougVisibility = true
                            }
                        },
                        onReject = {
                            viewModel.onReject(it)
                            alertMsg =
                                "Not selected this time. Your effort and feedback are truly appreciated! Your dedication is invaluable!"
                            dialougVisibility = true
                        },
                    )

                    AnimatedVisibility(visible = dialougVisibility) {
                        VideoDialouge(msg = alertMsg) {
                            dialougVisibility = false
                        }
                    }
                }

            }
        }
    }

}


@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videos: List<VideoData?>,
    onBackPress: () -> Unit = {}
) {
    val context = LocalContext.current

    val mediaItems = arrayListOf<MediaItem>()

//    val videoTitle = remember {
//        mutableStateOf(videos[0].name)
//    }
    val visibleState = remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }

    videos.forEach {
        it?.let {
            mediaItems.add(
                MediaItem.Builder()
                    .setUri(it.url)
                    .setMediaId(it.id.toString())
                    .setTag(it)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setDisplayTitle(it.name)
                            .build()
                    )
                    .build()
            )
        }
    }
    // create our player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            this.setMediaItems(mediaItems)
            this.prepare()
            this.playWhenReady = true
            addListener(
                object : Player.Listener {

                    override fun onEvents(
                        player: Player,
                        events: Player.Events
                    ) {
                        super.onEvents(player, events)
                        // hide title only when player duration is at least 200ms
                        if (player.currentPosition >= 200)
                            visibleState.value = false
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        reason: Int
                    ) {
                        super.onMediaItemTransition(
                            mediaItem,
                            reason
                        )
                        // everytime the media item changes show the title
//                        visibleState.value = true
                        /*       videoTitle.value =
                                   mediaItem?.mediaMetadata
                                       ?.displayTitle.toString()*/
                    }
                }
            )
        }
    }

    ConstraintLayout(modifier = modifier) {
        val (title, videoPlayer, controlerView) = createRefs()


        /*  // video title
          AnimatedVisibility(
              visible = visibleState.value,
              modifier =
              Modifier.constrainAs(title) {
                  top.linkTo(parent.top)
                  start.linkTo(parent.start)
                  end.linkTo(parent.end)
              }
          ) {
              Text(
                  text = videoTitle.value,
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  modifier =
                  Modifier
                      .padding(16.dp)
                      .fillMaxWidth()
                      .wrapContentHeight()
              )
          }*/


        // player view
        DisposableEffect(
            AndroidView(
                modifier =
                Modifier
                    .testTag("VideoPlayer")
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        visibleState.value = visibleState.value.not()
                    },
                factory = {

                    // exo player view for our video player
                    PlayerView(context).apply {
//                        useController = false
                        player = exoPlayer
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT,
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT
                            )
                    }
                }
            )


        ) {
            onDispose {
                // relase player when no longer needed
                exoPlayer.release()
            }
        }

        /* AnimatedVisibility(
             visible = visibleState.value,
             modifier = Modifier.constrainAs(controlerView) {
                 top.linkTo(parent.top)
                 start.linkTo(parent.start)
                 end.linkTo(parent.end)
                 bottom.linkTo(parent.bottom)
             },
             enter = fadeIn(),
             exit = fadeOut()
         ) {
             CenterControls(
                 isPlaying = { isPlaying },
                 onReplayClick = {
                     exoPlayer.seekBack()
                 },
                 onPauseToggle = {
                     if (exoPlayer.isPlaying) {
                         exoPlayer.pause()
                     } else {
                         exoPlayer.play()
                     }
                     isPlaying = isPlaying.not()
                 },
                 onForwardClick = { exoPlayer.seekForward() },
             )
         }*/


    }
}


@Composable
fun BottomActions(
    modifier: Modifier = Modifier,
    video: VideoData,
    isShortListed: Boolean,
    onReject: (Int) -> Unit,
    onApproveOrShortList: (Int) -> Unit,
) {
    Box(modifier = modifier) {

        Column(modifier = Modifier) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name : ",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = video.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Employer City : ",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = video.city,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Employer Id  : ",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = video.empId,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*      AnimatedVisibility(visible = isPrevShow()) {
                          Button(
                              onClick = onPrevClick, shape = RoundedCornerShape(10.dp),
                              colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey80)
                          ) {
                              Icon(
                                  imageVector = Icons.Default.KeyboardArrowLeft,
                                  contentDescription = "Prev"
                              )
                              Text(text = "Pre")
                          }

                      }*/
                if (isShortListed) {
                    Button(
                        onClick = { onApproveOrShortList(video.id) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = deepGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "aprove")
                        Text(text = "Approve")
                    }
                } else {
                    Button(
                        onClick = { onApproveOrShortList(video.id) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "aprove")
                        Text(text = "Short List")
                    }

                }
                Button(
                    onClick = { onReject(video.id) }, shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Reject")
                    Text(text = "Reject")
                }
                /*         AnimatedVisibility(visible = isNextShow()) {
                             Button(
                                 onClick = onNextClick, shape = RoundedCornerShape(10.dp),
                                 colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey80)
                             ) {
                                 Text(text = "Next")
                                 Icon(
                                     imageVector = Icons.Default.KeyboardArrowRight,
                                     contentDescription = "Next"
                                 )
                             }

                         }*/
            }

        }

    }
}


@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit
) {

    val duration = remember(totalDuration()) { totalDuration() }

    val videoTime = remember(currentTime()) { currentTime() }

    val buffer = remember(bufferPercentage()) { bufferPercentage() }

    Column(modifier = modifier.padding(bottom = 32.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // buffer bar
            Slider(
                value = buffer.toFloat(),
                enabled = false,
                onValueChange = { /*do nothing*/ },
                valueRange = 0f..100f,
                colors =
                SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Gray
                )
            )

            // seek bar
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = videoTime.toFloat(),
                onValueChange = onSeekChanged,
                valueRange = 0f..duration.toFloat(),
                colors =
                SliderDefaults.colors(
                    thumbColor = Purple80,
                    activeTickColor = Purple40
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = duration.formatMinSec(),
                color = Purple40
            )

            IconButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = {}
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Enter/Exit fullscreen"
                )
            }
        }
    }
}

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}

@Composable
fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onForwardClick: () -> Unit
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }

    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(modifier = Modifier.size(40.dp), onClick = onReplayClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_skipe_pev),
                contentDescription = "Forward 10 seconds"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onPauseToggle) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter =
                if (isVideoPlaying) {
                    painterResource(id = R.drawable.pause)
                } else {
                    painterResource(id = R.drawable.play)
                },
                contentDescription = "Play/Pause"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onForwardClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_skipe_nex),
                contentDescription = "Forward 10 seconds"
            )
        }
    }
}





