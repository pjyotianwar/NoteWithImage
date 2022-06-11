package com.pjyotianwar.notewithimage.daos

import androidx.room.*
import com.pjyotianwar.notewithimage.models.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("Select * from notes_table where user=:user")
    fun getAllNotes(user: String): Flow<List<Notes>>

    @Insert(entity = Notes::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(notes: Notes)

    @Delete(entity = Notes::class)
    suspend fun deleteNote(notes: Notes)

    @Update(entity = Notes::class)
    suspend fun updateNote(notes: Notes)
}
