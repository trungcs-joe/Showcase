package com.note.data

import com.note.common.result.Result
import com.note.data.repo.LocalNoteRepository
import com.note.data.repo.NoteRepository
import com.note.database.dao.NoteDao
import com.note.database.model.NoteEntity
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

class NoteRepositoryTest {
    private val noteDao: NoteDao = mock()

    private lateinit var repository: NoteRepository

    @Before
    fun setup() {
        repository = LocalNoteRepository(noteDao)
    }

    @Test
    fun NoteRepository_test_get_all_notes_success() = runTest {
        `when`(noteDao.getAllNoteEntities()).thenReturn(
            flow {
                emit(
                    listOf(
                        noteEntityFactory(1),
                        noteEntityFactory(2),
                    )
                )
            }
        )

        val results = repository.getAllNoteEntities().take(2).toList()

        assert(results.first() is Result.Loading)
        assert(results[1] is Result.Success)
    }

    @Test
    fun NoteRepository_test_get_all_notes_failed() = runTest {
        `when`(noteDao.getAllNoteEntities()).thenReturn(
            flow {
                throw Exception("Error")
            }
        )

        val results = repository.getAllNoteEntities().take(2).toList()

        assert(results.first() is Result.Loading)
        assert(results[1] is Result.Error)
    }

    @Test
    fun NoteRepository_test_get_note_by_id_success() = runTest {
        `when`(noteDao.getNoteEntity(1)).thenReturn(
            noteEntityFactory(1)
        )

        val result = repository.getNoteById(1)

        assert(result is Result.Success)
    }

    @Test
    fun NoteRepository_test_get_note_by_id_failed() = runTest {
        `when`(noteDao.getNoteEntity(1)).thenThrow(
            IllegalArgumentException("Error")
        )

        val result = repository.getNoteById(1)

        assert(result is Result.Error)
        assertEquals((result as Result.Error).exception.message, "Error")
    }

    @Test
    fun NoteRepository_test_insert_new_note() = runTest {
        `when`(noteDao.upsertNote(noteEntityFactory(2))).thenReturn(
            Unit
        )
        `when`(noteDao.getNoteEntity(2)).thenReturn(
            noteEntityFactory(2)
        )

        val result = repository.getNoteById(2)

        assert(result is Result.Success)
        assert((result as Result.Success).data.id == 2)
    }

    @Test
    fun NoteRepository_test_delete_note_success() = runTest {
        `when`(noteDao.deleteNotes(listOf(1, 2))).thenReturn(
            Unit
        )

        val result = repository.deleteNotes(listOf(1, 2))

        assert(result is Result.Success)
    }

    @Test
    fun NoteRepository_test_delete_note_failed() = runTest {
        `when`(noteDao.deleteNotes(listOf(1, 2))).thenThrow(
            IllegalArgumentException("Error")
        )

        val result = repository.deleteNotes(listOf(1, 2))

        assert(result is Result.Error)
    }

}

private fun noteEntityFactory(id: Int) = NoteEntity(
    id = id,
    title = "title",
    content = "content",
)