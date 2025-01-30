package com.serhiiromanchuk.echojournal.data

import com.serhiiromanchuk.echojournal.domain.audio.AudioTranscription
import io.github.vyfor.groqkt.GroqClient
import io.github.vyfor.groqkt.GroqModel
import io.github.vyfor.groqkt.api.GroqResponse
import javax.inject.Inject

class GroqAudioTranscription @Inject constructor(
    private val apiKey: String
) : AudioTranscription {
    private val client by lazy { GroqClient(apiKey) }

    override suspend fun transcribeAudio(audioFilePath: String): String {
        val result: Result<GroqResponse<io.github.vyfor.groqkt.api.audio.transcription.AudioTranscription>> = client.transcribeAudio {
            model = GroqModel.DISTIL_WHISPER_LARGE_V3_EN
            filename = "Audio"
            file(audioFilePath)
        }

        return when {
            result.isSuccess -> result.getOrNull()?.data?.text ?: ""
            result.isFailure -> result.exceptionOrNull()?.message ?: ""
            else -> ""
        }.trim()
    }
}