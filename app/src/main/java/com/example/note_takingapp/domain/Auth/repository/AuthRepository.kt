package com.example.note_takingapp.domain.Auth.repository

import com.example.note_takingapp.domain.Auth.model.User

interface AuthRepository {
    suspend fun register(email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}