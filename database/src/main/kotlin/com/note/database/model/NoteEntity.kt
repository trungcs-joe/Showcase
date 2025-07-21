package com.note.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.note.model.Note
import java.util.Date

@Entity(
    tableName = "notes",
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val lastModified: Date = Date(),
)

fun NoteEntity.toModel(): Note = Note(
    id = id,
    title = title,
    content = content,
    lastModified = lastModified,
)

