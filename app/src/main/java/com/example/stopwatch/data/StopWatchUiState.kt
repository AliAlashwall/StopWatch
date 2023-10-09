package com.example.stopwatch.data


data class StopWatchUiState(
    var isRunning: Boolean = false,
    var mainButtonClicked: Boolean = false,
    var pauseClicked: Boolean = false,
    var startClicked: Boolean = false,
    var lapClicked: Boolean = false,
    var startTime: Long = 0L,
    var elapsedMillis: Long = 0L,
    var mainButtonText: String = "Start",
    var secButtonText: String = "Lap",
    var lapList: MutableList<Long> = mutableListOf<Long>(),
    var countLap: Int = 0,
    val inDark:Boolean = false,
    var pausedTime: Long = 0L
)