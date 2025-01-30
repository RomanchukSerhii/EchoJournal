package com.serhiiromanchuk.echojournal.di

import android.content.Context
import com.serhiiromanchuk.echojournal.BuildConfig
import com.serhiiromanchuk.echojournal.data.AndroidAudioPlayer
import com.serhiiromanchuk.echojournal.data.AndroidAudioRecorder
import com.serhiiromanchuk.echojournal.data.GroqAudioTranscription
import com.serhiiromanchuk.echojournal.domain.audio.AudioPlayer
import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import com.serhiiromanchuk.echojournal.domain.audio.AudioTranscription
import com.serhiiromanchuk.echojournal.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {
    @Provides
    @Singleton
    fun provideAudioRecorder(
        @ApplicationContext context: Context
    ): AudioRecorder {
        return AndroidAudioRecorder(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: Context
    ): AudioPlayer {
        return AndroidAudioPlayer(context)
    }

    @Provides
    @Singleton
    @Named(Constants.API_KEY)
    fun provideApiKey(): String = BuildConfig.GROQ

    @Provides
    @Singleton
    fun provideAudioTranscription(
        @Named(Constants.API_KEY) apiKey: String
    ): AudioTranscription {
        return GroqAudioTranscription(apiKey)
    }
}