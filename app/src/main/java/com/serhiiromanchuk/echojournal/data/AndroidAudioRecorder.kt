package com.serhiiromanchuk.echojournal.data

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AndroidAudioRecorder @Inject constructor(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun createAudioFile(): String {
        val fileName = "audio_${System.currentTimeMillis()}.mp3"
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        return File(outputDir, fileName).absolutePath
    }

    override fun start(outputFilePath: String) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("AudioRecorder", "prepare() failed")
            }

            start()
            recorder = this
        }
    }

    override fun pause() {
        recorder?.pause()
    }

    override fun resume() {
        recorder?.resume()
    }

    override fun stop() {
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null
    }
}