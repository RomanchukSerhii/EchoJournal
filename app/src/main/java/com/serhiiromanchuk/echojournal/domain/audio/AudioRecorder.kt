package com.serhiiromanchuk.echojournal.domain.audio

import java.io.File

interface AudioRecorder {
    fun createAudioFile(): File
    fun start(outputFile: File)
    fun pause()
    fun resume()
    fun stop()
}