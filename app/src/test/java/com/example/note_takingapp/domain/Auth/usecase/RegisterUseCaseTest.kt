package com.example.note_takingapp.domain.Auth.usecase

import com.example.note_takingapp.domain.Auth.model.User
import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class RegisterUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        registerUseCase = RegisterUseCase(authRepository)
    }

    @Test
    fun `register returns failure when email or password is blank`() = runTest {
        val result = registerUseCase("", "")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Email and password cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register returns success when valid email and password are provided`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = User(email = email, uid = "uid123")

        whenever(authRepository.register(email, password)).thenReturn(Result.success(user))

        val result = registerUseCase(email, password)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `register returns failure when repository throws error`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val exception = Exception("Registration failed")

        whenever(authRepository.register(email, password)).thenReturn(Result.failure(exception))

        val result = registerUseCase(email, password)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}