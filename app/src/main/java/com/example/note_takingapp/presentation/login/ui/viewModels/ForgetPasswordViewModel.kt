package com.example.note_takingapp.presentation.login.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_takingapp.domain.Auth.model.User
import com.example.note_takingapp.domain.Auth.usecase.ForgotPasswordUseCase
import com.example.note_takingapp.presentation.login.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgetPasswordViewModel @Inject constructor(private val forgotPasswordUseCase: ForgotPasswordUseCase) : ViewModel() {


    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState


    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            forgotPasswordUseCase(email)
                .onSuccess {
                    _authState.value = AuthState.Success(User(email = email))
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Failed to send email")
                }
        }
    }


}