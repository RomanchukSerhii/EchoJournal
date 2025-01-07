package com.serhiiromanchuk.echojournal.di

import android.content.Context
import androidx.room.Room
import com.serhiiromanchuk.echojournal.data.database.TopicDao
import com.serhiiromanchuk.echojournal.data.database.TopicDatabase
import com.serhiiromanchuk.echojournal.data.repository.TopicDbRepositoryImpl
import com.serhiiromanchuk.echojournal.domain.repository.TopicDbRepository
import com.serhiiromanchuk.echojournal.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TopicDatabase {
        return Room.databaseBuilder(
            context,
            TopicDatabase::class.java,
            Constants.TOPIC_DB
        ).build()
    }

    @Provides
    @Singleton
    fun provideTopicDao(database: TopicDatabase): TopicDao = database.getTopicDao()

    @Provides
    fun provideTopicDbRepository(
        topicDao: TopicDao
    ): TopicDbRepository = TopicDbRepositoryImpl(topicDao)
}