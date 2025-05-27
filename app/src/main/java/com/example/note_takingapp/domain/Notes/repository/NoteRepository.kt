package com.example.note_takingapp.domain.Notes.repository

import com.example.note_takingapp.domain.Notes.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun addNote(note: Note): Result<String>
    suspend fun deleteNote(noteId: Long): Result<Unit>
    suspend fun updateNote(note: Note): Result<Unit>
    suspend fun getNoteById(noteId: Long): Result<Note>
    fun getAllNotes(): Flow<List<Note>>

}