package com.example.note_takingapp.presentation.login.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_takingapp.domain.Auth.use_case.RegisterUseCase
import com.example.note_takingapp.presentation.login.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase,): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState


    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            registerUseCase(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.Success(user)
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Registration failed")
                }
        }
    }

}