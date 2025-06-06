package com.example.note_takingapp.domain.Auth.usecase

import com.example.note_takingapp.domain.Auth.model.User
import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        return repository.register(email, password)
    }
}