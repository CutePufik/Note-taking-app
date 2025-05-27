package com.example.note_takingapp.domain.Notes.usecase

import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke() : Flow<List<Note>>{
        return repository.getAllNotes()
    }
}