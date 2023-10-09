package com.example.stopwatch

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stopwatch.ui.theme.lap_reset_dark_button
import com.example.stopwatch.ui.theme.lap_reset_light_button
import com.example.stopwatch.ui.theme.md_theme_dark_outline
import com.example.stopwatch.ui.theme.md_theme_light_outline
import java.util.concurrent.TimeUnit

@Composable
fun StopWatchScreen(swViewModel : StopWatchViewModel = viewModel()) {

    val sWUiState by swViewModel.sWUiState.collectAsState()

    swViewModel.LaunchedEffect()
    StopWatchLayout(
        mainButtonText = swViewModel.handleMainButText(),
        secButtonText = swViewModel.handleSecButText(),
        inDark = isSystemInDarkTheme(),
        elapsedMillis = sWUiState.elapsedMillis,
        lapClicked = sWUiState.lapClicked,
        startClicked =  sWUiState.startClicked,
        lapList = sWUiState.lapList,
        countLap = sWUiState.countLap,
        onMainButClicked = { swViewModel.onMainButtonClicked() },
        lapResetHandleClick = { swViewModel.LapResetButHandeling() },
        )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopWatchLayout(
    mainButtonText : String,
    secButtonText :String,
    inDark :Boolean,
    elapsedMillis : Long,
    lapClicked : Boolean,
    startClicked : Boolean,
    lapList : MutableList<Long>,
    countLap : Int,
    onMainButClicked : () -> Unit,
    lapResetHandleClick : () ->Unit,

){
    Scaffold(
        modifier = Modifier,
        topBar = {
            StopwatchAppBar(inDark)
        },
        content = { PaddingValues(0.dp) }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .padding(top = 90.dp, bottom = 20.dp)
                .size(300.dp),
            shape = RoundedCornerShape(200.dp),
            border = BorderStroke( 4.dp,
                if ( inDark ) md_theme_dark_outline else md_theme_light_outline
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = annotateText(text = formatElapsedTimee(elapsedMillis)),
                    modifier = Modifier
                        .padding(top = 100.dp)
                        .fillMaxWidth(),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)){
            if (lapClicked){
                LapItems(lapList, countLap)
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 22.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(                              // START && Pause
                onClick = { onMainButClicked()},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .weight(1f) ,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = mainButtonText,
                    fontSize = 20.sp,
                )
            }
            if (startClicked){
                Button(
                    onClick = { lapResetHandleClick()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(if(inDark) lap_reset_dark_button else lap_reset_light_button),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = secButtonText,
                        fontSize = 20.sp,
                        color = Color(0xFFE23A4C)
                    )
                }
            }
        }
    }
}

@Composable
fun LapItems(lapList : List<Long>, countLap : Int){
    LazyColumn{
        items(lapList.sortedDescending()) {lapItem ->
            val lapTime = formatElapsedTimee(lapItem)
            val lapNum = String.format("%02d",(lapList.indexOf(lapItem) + 1))

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Text(
                    text = lapNum,
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = lapTime,
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Text(
                    text = "+$lapTime",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    fontSize = 20.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}
@Composable
fun formatElapsedTimee(elapsedMillis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60
    val milliseconds = elapsedMillis % 1000

    return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds /10)
}
@Composable
fun annotateText(text : String) : AnnotatedString {
    val annotatedString = buildAnnotatedString {
        append(text.substring(0, text.length - 2))

        withStyle(style = SpanStyle(color = Color(0xFFFB0100))) {
            append(text[text.length - 2])
        }
        withStyle(style = SpanStyle(color = Color(0xFFFB0100))) {
            append(text[text.length - 1])
        }
    }
    return annotatedString
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchAppBar(inDark: Boolean){
    CenterAlignedTopAppBar(
        title = {
            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top =10.dp)
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        if (inDark) painterResource(R.drawable.stop_watch_dark) else painterResource(
                            R.drawable.stop_watch_light
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(24.dp),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.stop_watch),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    )
}

