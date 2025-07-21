package com.trungcs.showcaseproject.repo

import com.note.common.result.Result
import com.note.data.repo.NoteRepository
import com.note.model.Note
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest

class MockNoteRepository : NoteRepository {
    private val listNote = MutableSharedFlow<List<Note>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    init {
        listNote.tryEmit(listOf())
    }

    override fun getAllNoteEntities(): Flow<Result<List<Note>>> {
        return listNote.mapLatest {
            Result.Success(it)
        }
    }

    override suspend fun getNoteById(id: Int): Result<Note> {
        val listNoteValue = listNote.replayCache.firstOrNull()?.toMutableList()
            ?: return Result.Error(Exception(""))

        val current = listNoteValue.firstOrNull { it.id == id }
        return if (current == null)
            Result.Error(IllegalArgumentException("Not Found"))
        else Result.Success(current)
    }

    override suspend fun upsertNote(note: Note): Result<Unit> {
        val listNoteValue = listNote.replayCache.firstOrNull()?.toMutableList()
            ?: return Result.Error(Exception(""))

        val index = listNoteValue.indexOfLast { it.id == note.id }
        if (index != -1) {
            listNoteValue[index] = note
        } else {
            listNoteValue.add(note)
        }

        listNote.tryEmit(listNoteValue)
        return Result.Success(Unit)
    }

    override suspend fun deleteNotes(ids: List<Int>): Result<Unit> {
        val listNoteValue = listNote.replayCache.firstOrNull()?.toMutableList()
            ?: return Result.Error(Exception(""))
        listNoteValue.filter { !ids.contains(it.id) }
        listNote.tryEmit(listNoteValue.filter { !ids.contains(it.id) })
        return Result.Success(Unit)
    }
}