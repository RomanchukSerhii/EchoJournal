package com.serhiiromanchuk.echojournal.domain.audio

interface AudioRecorder {
    fun createAudioFile(): String
    fun start(outputFilePath: String)
    fun pause()
    fun resume()
    fun stop(): String
}