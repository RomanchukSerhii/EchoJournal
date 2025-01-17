package com.serhiiromanchuk.echojournal.domain.audio

interface AudioRecorder {
    fun start()
    fun pause()
    fun resume()
    fun stop(saveFile: Boolean): String
    fun getAmplitudeLogFilePath(): String
}