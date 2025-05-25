package com.example.note_takingapp.domain.Auth.use_case

import com.example.note_takingapp.domain.Auth.model.User
import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        return repository.login(email, password)
    }
}