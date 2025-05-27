package com.example.note_takingapp.presentation.notes.updateNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.usecase.GetNoteByIdUseCase
import com.example.note_takingapp.domain.Notes.usecase.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel(){

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Получение заметки по ID
     */
    fun loadNoteById(noteId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getNoteByIdUseCase(noteId)
            result.onSuccess {
                _note.value = it
            }.onFailure {
                _error.value = it.message ?: "Ошибка при получении заметки"
            }
            _isLoading.value = false
        }
    }

    /**
     * Сохранение (обновление) заметки
     */
    fun saveNote(note: Note) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                updateNoteUseCase(note)
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка при сохранении"
            } finally {
                _isLoading.value = false
            }
        }
    }


}