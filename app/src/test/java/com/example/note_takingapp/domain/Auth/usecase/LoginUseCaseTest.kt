package com.example.note_takingapp.domain.Auth.usecase

import com.example.note_takingapp.domain.Auth.model.User
import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever


class LoginUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `login returns failure when email or password is blank`() = runTest {
        val result = loginUseCase("", "")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Email and password cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login returns success when credentials are correct`() = runTest {
        val user = User("user@example.com")
        whenever(authRepository.login("user@example.com", "password")).thenReturn(Result.success(user))

        val result = loginUseCase("user@example.com", "password")

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `login returns failure when repository returns error`() = runTest {
        val exception = Exception("Invalid credentials")
        whenever(authRepository.login("user@example.com", "wrongpass")).thenReturn(Result.failure(exception))

        val result = loginUseCase("user@example.com", "wrongpass")

        assertTrue(result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
    }
}
