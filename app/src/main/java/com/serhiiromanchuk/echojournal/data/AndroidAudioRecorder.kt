package com.serhiiromanchuk.echojournal.data

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
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

    override fun createAudioFile(): File {
        val fileName = "audio_${System.currentTimeMillis()}.mp3"
        val outputDir = context.filesDir
        return File(outputDir, fileName)
    }

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absoluteFile)

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