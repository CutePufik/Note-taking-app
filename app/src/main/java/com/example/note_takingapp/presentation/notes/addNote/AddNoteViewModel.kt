package com.example.note_takingapp.presentation.addnote


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.usecase.AddNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject


class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {

    private val _addNoteResult = MutableStateFlow<Result<String>?>(null)
    val addNoteResult: StateFlow<Result<String>?> = _addNoteResult

    fun addNote(title: String, text: String, userId: String) {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            text = text,
            createdAt = Date(),
            userId = userId
        )

        viewModelScope.launch {
            val result = addNoteUseCase(note)
            _addNoteResult.value = result
        }
    }

    fun clearResult() {
        _addNoteResult.value = null
    }
}
