package com.example.note_takingapp.di

import androidx.lifecycle.ViewModel
import com.example.note_takingapp.presentation.login.ui.viewModels.ForgetPasswordViewModel
import com.example.note_takingapp.presentation.login.ui.viewModels.LoginViewModel
import com.example.note_takingapp.presentation.login.ui.viewModels.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    @Binds
    fun bindLoginViewModel(impl: LoginViewModel): ViewModel

    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    @Binds
    fun bindRegisterViewModel(impl: RegisterViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel::class)
    @Binds
    fun bindForgetPasswordViewModel(impl: ForgetPasswordViewModel): ViewModel




}