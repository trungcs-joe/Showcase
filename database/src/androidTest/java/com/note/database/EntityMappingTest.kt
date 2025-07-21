package com.note.database

import com.note.database.model.NoteEntity
import com.note.database.model.toModel
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class EntityMappingTest {
    @Test
    fun NoteEntity_to_model_test() {
        val currentDate = Date()
        val entity = NoteEntity(1, "test_title", "test_content", currentDate)
        val model = entity.toModel()

        assertEquals(model.id, 1)
        assertEquals(model.title, "test_title")
        assertEquals(model.content, "test_content")
        assertEquals(model.lastModified, currentDate)

    }
}