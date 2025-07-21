package com.note.data.repo

import com.note.common.result.Result
import com.note.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    // Get and Observe changes of note table
    fun getAllNoteEntities(): Flow<Result<List<Note>>>

    // Get node by id
    suspend fun getNoteById(id: Int): Result<Note>

    // Upsert existing note, use id to classify that it's the insert of the update
    suspend fun upsertNote(note: Note): Result<Unit>

    // Delete notes by ids
    suspend fun deleteNotes(ids: List<Int>): Result<Unit>
}