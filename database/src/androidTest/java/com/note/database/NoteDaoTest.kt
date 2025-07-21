package com.note.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.note.database.dao.NoteDao
import com.note.database.model.NoteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NoteDaoTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NoteDatabase::class.java,
        ).build()
        noteDao = db.noteDao()
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun noteDao_get_item_by_id_success() = runTest {
        val note = testNoteResource(1, "newTitle", "newContent")
        noteDao.upsertNote(note)
        val insertedNote = noteDao.getNoteEntity(1)

        assertEquals(insertedNote.title, "newTitle")
        assertEquals(insertedNote.content, "newContent")
    }

    @Test(expected = IllegalStateException::class)
    fun noteDao_get_item_by_id_failed() = runTest {
        val note = testNoteResource(1)
        noteDao.upsertNote(note)
        noteDao.getNoteEntity(2)
    }

    @Test
    fun noteDao_upsert_note_success() = runTest {
        val note = testNoteResource(1)
        noteDao.upsertNote(note)
        noteDao.upsertNote(note.copy(title = "updatedTitle", content = "updatedContent"))
        val newNode = noteDao.getNoteEntity(1)

        assertEquals(newNode.title, "updatedTitle")
        assertEquals(newNode.content, "updatedContent")
    }

    @Test
    fun noteDao_insert_note_auto_generate_id() = runTest {
        val note1 = testNoteResource()
        val note2 = testNoteResource()
        val note3 = testNoteResource()
        val note4 = testNoteResource()
        noteDao.upsertNote(note1)
        noteDao.upsertNote(note2)
        noteDao.upsertNote(note3)
        noteDao.upsertNote(note4)

        val allNotes = noteDao.getAllNoteEntities().first()

        assertEquals(allNotes.size, 4)
        assertEquals(listOf(1, 2, 3, 4), allNotes.map { it.id }.toList())
    }

    @Test
    fun noteDao_create_upsert_negative_id() = runTest {
        val note = testNoteResource(-1)
        noteDao.upsertNote(note)
    }

    @Test
    fun noteDao_get_all_notes_empty() = runTest {
        val allNote = noteDao.getAllNoteEntities().first()
        assert(allNote.isEmpty())
    }

    @Test
    fun noteDao_get_all_notes_not_empty() = runTest {
        val note1 = testNoteResource()
        val note2 = testNoteResource()
        val note3 = testNoteResource()
        noteDao.upsertNote(note1)
        noteDao.upsertNote(note2)
        noteDao.upsertNote(note3)

        val allNote = noteDao.getAllNoteEntities().first()
        assertEquals(allNote.size, 3)
    }

    @Test
    fun noteDao_delete_note_success() = runTest {
        val note1 = testNoteResource(1)
        noteDao.upsertNote(note1)
        noteDao.deleteNotes(listOf(1))

        val allNote = noteDao.getAllNoteEntities().first()
        assertEquals(allNote.size, 0)
    }
}

private fun testNoteResource(
    id: Int = 0,
    title: String = "Title",
    content: String = "Content",
) = NoteEntity(
    id = id,
    title = title,
    content = content,
)
