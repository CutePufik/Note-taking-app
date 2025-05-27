package com.example.note_takingapp.domain.Notes.usecase

import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: String): Result<Note> {
        return repository.getNoteById(noteId)
    }
}