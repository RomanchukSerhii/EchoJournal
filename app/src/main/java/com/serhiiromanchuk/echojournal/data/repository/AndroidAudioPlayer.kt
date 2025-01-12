package com.serhiiromanchuk.echojournal.data.repository

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.serhiiromanchuk.echojournal.domain.audio.AudioPlayer
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun pause() {
        player?.pause()
    }

    override fun resume() {
        player?.start()
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}