package com.example.stopwatch

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.stopwatch.data.StopWatchUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StopWatchViewModel :ViewModel(){

    private val _sWUiState = MutableStateFlow(StopWatchUiState())
    val sWUiState : StateFlow<StopWatchUiState> = _sWUiState.asStateFlow()

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun LaunchedEffect(){
        LaunchedEffect(_sWUiState.value.isRunning) {
            while (_sWUiState.value.isRunning) {
                withFrameMillis { frameTime ->
                    if (_sWUiState.value.startTime == 0L) {
                        _sWUiState.update {
                            it.copy(
                            startTime = frameTime
                            )
                        }
                    }
                    _sWUiState.update {
                        it.copy(
                            elapsedMillis = frameTime - _sWUiState.value.startTime + _sWUiState.value.pausedTime
                        )}
                }
                delay(1)
            }
        }
    }
    fun onMainButtonClicked(){
        _sWUiState.value.apply {
            if (mainButtonText == "Start") {
                _sWUiState.update {
                    it.copy(
                        startClicked = true,
                        startTime = 0L,
                        pausedTime = 0L,
                        mainButtonClicked = !mainButtonClicked,
                        isRunning = !isRunning,
                        pauseClicked = false

                    )
                }
            }
            else{     // PAUSE CLICKED
                _sWUiState.update {
                    it.copy(
                        isRunning = !isRunning,
                        mainButtonText = if (isRunning) "Pause" else "Resume",
                        pauseClicked = !pauseClicked,
                        pausedTime = elapsedMillis,
                        startTime = 0L,
                    )
                }
            }
        }
    }
    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun handleMainButText() : String{
        return if (_sWUiState.value.mainButtonClicked) stringResource(R.string.pause) else stringResource(R.string.start)
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun handleSecButText() : String{
        return if(_sWUiState.value.pauseClicked) stringResource(R.string.reset) else stringResource(R.string.lap)
    }

    fun LapResetButHandeling(){
        if (_sWUiState.value.secButtonText == "Reset") {
            _sWUiState.update {
                it.copy(
                    startClicked = false,
                    lapClicked = false,
                    countLap = 0,
                    startTime = 0L,
                    pausedTime = 0L,
                    elapsedMillis = 0L,
                    mainButtonClicked = !it.mainButtonClicked,
                    lapList = mutableListOf<Long>() ,
                )
            }
        }
        else     // LAP
        {
            _sWUiState.update {
                val updatedList = it.lapList.toMutableList()
                updatedList.add(it.elapsedMillis)
                it.copy(
                    lapClicked = true,
                    lapList = updatedList,
                    countLap = it.countLap++,
                )
            }
            }

        }
}

