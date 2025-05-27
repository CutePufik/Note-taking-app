package com.example.note_takingapp.domain.Notes.usecase

import com.example.note_takingapp.domain.Notes.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: String): Result<Unit> {
        return repository.deleteNote(noteId)
    }
}