package com.serhiiromanchuk.echojournal.domain.audio

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun pause()
    fun resume()
    fun stop()
}