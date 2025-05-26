package com.example.note_takingapp.presentation.login.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_takingapp.domain.Auth.usecase.LoginUseCase
import com.example.note_takingapp.presentation.login.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            loginUseCase(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.Success(user)
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Login failed")
                }
        }
    }
}