package com.note.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.note.database.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Get a single note by id
    @Query(
        value = """
        SELECT * FROM notes
        WHERE id = :noteId
    """,
    )
    suspend fun getNoteEntity(noteId: Int): NoteEntity

    // Get and observe all notes from databases by using Flow.
    @Query(value = "SELECT * FROM notes")
    fun getAllNoteEntities(): Flow<List<NoteEntity>>

    // Inserts or updates [entities] in the db under the specified primary keys
    @Upsert
    suspend fun upsertNote(entities: NoteEntity)

    // Deletes rows in the db matching the specified [ids]
    @Query(
        value = """
            DELETE FROM notes
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteNotes(ids: List<Int>)
}