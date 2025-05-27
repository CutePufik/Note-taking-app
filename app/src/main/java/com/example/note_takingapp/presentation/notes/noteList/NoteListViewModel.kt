package com.example.note_takingapp.presentation.notes.noteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.usecase.DeleteNoteUseCase
import com.example.note_takingapp.domain.Notes.usecase.GetAllNotesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class NoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            getAllNotesUseCase()
                .catch { e ->
                    _error.value = e.message ?: "Неизвестная ошибка"
                    _isLoading.value = false
                }
                .collect { noteList ->
                    _notes.value = noteList
                    _isLoading.value = false
                }
        }
    }
    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
            loadNotes()
        }
    }

}
