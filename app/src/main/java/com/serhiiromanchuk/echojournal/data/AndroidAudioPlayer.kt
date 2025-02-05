package com.serhiiromanchuk.echojournal.data

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.serhiiromanchuk.echojournal.domain.audio.AudioPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var filePath: String = ""

    private var player: MediaPlayer? = null
    private var onCompletionListener: (() -> Unit)? = null

    private val _currentPositionFlow = MutableStateFlow(0)
    override val currentPositionFlow: StateFlow<Int> = _currentPositionFlow.asStateFlow()

    private var updateJob: Job? = null

    private var isCurrentlyPlaying: Boolean = false

    override fun initializeFile(filePath: String) {
        _currentPositionFlow.value = 0
        this.filePath = filePath
        createPlayer()
    }

    override fun play() {
        if (player == null) {
            createPlayer()
        }
        player?.start()
        player?.metrics
        startUpdatingCurrentPosition()
        isCurrentlyPlaying = true
    }

    override fun pause() {
        checkPlayerReady()
        player?.pause()
        stopUpdatingCurrentPosition()
    }

    override fun resume() {
        checkPlayerReady()
        player?.start()
        startUpdatingCurrentPosition()
    }

    override fun stop() {
        checkPlayerReady()
        stopUpdatingCurrentPosition()
        _currentPositionFlow.value = 0

        player?.stop()
        player?.release()
        player = null
        isCurrentlyPlaying = false

    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }

    override fun getDuration(): Int {
        checkPlayerReady()
        return player?.duration ?: 0
    }

    override fun isPlaying(): Boolean {
        return player?.isPlaying == true || isCurrentlyPlaying
    }

    private fun createPlayer() {
        val mediaPlayer = MediaPlayer.create(context, filePath.toUri())
            ?: throw IllegalArgumentException("Failed to create MediaPlayer. Invalid file path: $filePath")
        mediaPlayer.apply {
            player = this
            setOnCompletionListener {
                onCompletionListener?.invoke()
            }
        }
    }

    private fun startUpdatingCurrentPosition() {
        // Make sure the previous update is stopped
        checkPlayerReady()
        stopUpdatingCurrentPosition()

        // FEEDBACK: Inject scope for testability
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (player != null && player?.isPlaying == true) {
                _currentPositionFlow.value = player?.currentPosition ?: 0
                delay(10L)
            }
        }
    }

    private fun stopUpdatingCurrentPosition() {
        updateJob?.cancel()
        updateJob = null
    }

    private fun checkPlayerReady() {
        if (player == null) {
            throw IllegalStateException("MediaPlayer is not initialized. Call initializeFile() first.")
        }
    }
}