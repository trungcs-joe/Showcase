package com.note.data.di

import com.note.data.repo.LocalNoteRepository
import com.note.data.repo.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNoteRepository(
        userDataRepository: LocalNoteRepository,
    ): NoteRepository

}
