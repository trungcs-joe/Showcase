package com.note.data

import com.note.data.repo.toEntity
import com.note.model.Note
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class ModelMappingTest {
    @Test
    fun NoteModel_to_entity_test() {
        val currentDate = Date()
        val model = Note(1, "test_title", "test_content", currentDate)
        val entity = model.toEntity()

        assertEquals(entity.id, 1)
        assertEquals(entity.title, "test_title")
        assertEquals(entity.content, "test_content")
        assertEquals(entity.lastModified, currentDate)
    }
}