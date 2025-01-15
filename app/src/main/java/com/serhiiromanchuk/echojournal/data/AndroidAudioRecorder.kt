package com.serhiiromanchuk.echojournal.data

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.log10

class AndroidAudioRecorder @Inject constructor(
    private val context: Context
) : AudioRecorder {

    private val scope = CoroutineScope(Dispatchers.IO)

    private var recorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var amplitudeLogFile: File? = null
    private var amplitudeJob: Job? = null
    private var isCurrentlyRecording: Boolean = false
    private var isCurrentlyPaused: Boolean = false

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun createAudioFile(): String {
        val fileName = "audio_${System.currentTimeMillis()}.mp3"
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        audioFile = File(outputDir, fileName)
        return audioFile?.absolutePath ?: ""
    }

    override fun start(outputFilePath: String) {
        val amplitudeLogFileName = "amplitude_log_${System.currentTimeMillis()}.txt"
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        amplitudeLogFile = File(outputDir, amplitudeLogFileName)

        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)

            prepare()
            start()

            recorder = this
        }

        isCurrentlyRecording = true
        startLoggingAmplitude()
    }

    override fun pause() {
        recorder?.pause() ?: throw IllegalStateException("Recorder is not initialized. Call `start()` first.")
        isCurrentlyPaused = true
    }

    override fun resume() {
        recorder?.resume() ?: throw IllegalStateException("Recorder is not initialized. Call `start()` first.")
        isCurrentlyPaused = false
    }

    override fun stop(): String {
        amplitudeJob?.cancel()
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null
        isCurrentlyRecording = false

        return amplitudeLogFile?.absolutePath
            ?: throw IllegalStateException("Amplitude log file was not created. Ensure `start()` was called before `stop()`.")
    }

    private fun startLoggingAmplitude() {
        amplitudeJob?.cancel()
        amplitudeJob = scope.launch {
            while (isActive && isRecording()) {
                if (!isPaused()) {
                    val amplitude = recorder?.maxAmplitude?.toFloat() ?: 0f
                    if (amplitude > 0) {
                        val normalized = (log10(amplitude + 1) / log10(32767f + 1)).coerceIn(0f, 1f)
                        amplitudeLogFile?.appendText("$normalized,")
                    }
                }
                delay(100)
            }
        }
    }

    private fun isRecording(): Boolean = isCurrentlyRecording
    private fun isPaused(): Boolean = isCurrentlyPaused
}