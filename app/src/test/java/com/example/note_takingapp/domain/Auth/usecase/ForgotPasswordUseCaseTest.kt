package com.example.note_takingapp.domain.Auth.usecase

import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ForgotPasswordUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var forgotPasswordUseCase: ForgotPasswordUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        forgotPasswordUseCase = ForgotPasswordUseCase(authRepository)
    }

    @Test
    fun `invoke returns failure when email is blank`() = runTest {
        val result = forgotPasswordUseCase("")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Email cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke returns success when valid email is provided`() = runTest {
        val email = "test@example.com"

        whenever(authRepository.sendPasswordResetEmail(email)).thenReturn(Result.success(Unit))

        val result = forgotPasswordUseCase(email)

        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository throws error`() = runTest {
        val email = "test@example.com"
        val exception = Exception("Reset failed")

        whenever(authRepository.sendPasswordResetEmail(email)).thenReturn(Result.failure(exception))

        val result = forgotPasswordUseCase(email)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}