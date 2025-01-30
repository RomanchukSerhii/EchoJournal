package com.serhiiromanchuk.echojournal.domain.audio

interface AudioTranscription {
    suspend fun transcribeAudio(audioFilePath: String): String
}