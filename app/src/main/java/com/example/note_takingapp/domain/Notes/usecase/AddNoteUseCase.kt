package com.example.note_takingapp.domain.Notes.usecase

import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<String> {
        return repository.addNote(note)
    }
}
