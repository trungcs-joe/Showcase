package com.note.database.di

import com.note.database.NoteDatabase
import com.note.database.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesNotesDao(
        database: NoteDatabase,
    ): NoteDao = database.noteDao()

}
