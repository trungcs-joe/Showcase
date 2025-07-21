package com.note.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.note.database.dao.NoteDao
import com.note.database.model.NoteEntity
import com.note.database.util.DateConverters

@Database(
    entities = [
        NoteEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    DateConverters::class,
)
internal abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
