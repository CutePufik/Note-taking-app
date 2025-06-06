package com.example.note_takingapp.presentation.notes.state

import com.example.note_takingapp.domain.Auth.model.User

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}