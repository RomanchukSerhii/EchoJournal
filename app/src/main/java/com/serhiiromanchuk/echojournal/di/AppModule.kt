package com.serhiiromanchuk.echojournal.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.serhiiromanchuk.echojournal.data.database.EntryDao
import com.serhiiromanchuk.echojournal.data.database.EntryDatabase
import com.serhiiromanchuk.echojournal.data.database.TopicDao
import com.serhiiromanchuk.echojournal.data.database.TopicDatabase
import com.serhiiromanchuk.echojournal.data.repository.EntryRepositoryImpl
import com.serhiiromanchuk.echojournal.data.repository.SettingsRepositoryImpl
import com.serhiiromanchuk.echojournal.data.repository.TopicRepositoryImpl
import com.serhiiromanchuk.echojournal.domain.repository.EntryRepository
import com.serhiiromanchuk.echojournal.domain.repository.SettingsRepository
import com.serhiiromanchuk.echojournal.domain.repository.TopicRepository
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
    fun provideTopicDatabase(
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
    fun provideEntryDatabase(
        @ApplicationContext context: Context
    ): EntryDatabase {
        return Room.databaseBuilder(
            context,
            EntryDatabase::class.java,
            Constants.ENTRY_DB
        ).build()
    }

    @Provides
    @Singleton
    fun provideTopicDao(database: TopicDatabase): TopicDao = database.getTopicDao()

    @Provides
    fun provideTopicRepository(
        topicDao: TopicDao
    ): TopicRepository = TopicRepositoryImpl(topicDao)

    @Provides
    @Singleton
    fun provideEntryDao(database: EntryDatabase): EntryDao = database.getEntryDao()

    @Provides
    fun provideEntryRepository(
        entryDao: EntryDao
    ): EntryRepository = EntryRepositoryImpl(entryDao)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(Constants.APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSettingsRepository(
        sharedPreferences: SharedPreferences
    ): SettingsRepository {
        return SettingsRepositoryImpl(sharedPreferences)
    }
}