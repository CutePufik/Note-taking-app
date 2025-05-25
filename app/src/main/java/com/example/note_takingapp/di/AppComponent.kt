package com.example.note_takingapp.di

import com.example.note_takingapp.presentation.login.ui.forgetPassword.ForgetPasswordFragment
import com.example.note_takingapp.presentation.login.ui.login.LoginFragment
import com.example.note_takingapp.presentation.login.ui.register.RegisterFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ViewModelModule::class, DomainModule::class, FirebaseModule::class])
interface AppComponent {

    fun inject(fragment: LoginFragment)

    fun inject(fragment: RegisterFragment)

    fun inject(fragment: ForgetPasswordFragment)

}
