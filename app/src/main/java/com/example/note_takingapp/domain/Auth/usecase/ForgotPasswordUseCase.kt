package com.example.note_takingapp.domain.Auth.usecase

import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        return repository.sendPasswordResetEmail(email)
    }
}