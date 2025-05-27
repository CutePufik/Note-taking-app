package com.example.note_takingapp.di

import com.example.note_takingapp.presentation.login.ui.forgetPassword.ForgetPasswordFragment
import com.example.note_takingapp.presentation.login.ui.login.LoginFragment
import com.example.note_takingapp.presentation.login.ui.register.RegisterFragment
import com.example.note_takingapp.presentation.notes.addNote.AddNoteFragment
import com.example.note_takingapp.presentation.notes.noteList.NoteListFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ViewModelModule::class, DomainModule::class, FirebaseModule::class])
interface AppComponent {

    fun inject(fragment: LoginFragment)

    fun inject(fragment: RegisterFragment)

    fun inject(fragment: ForgetPasswordFragment)

    fun inject(fragment: NoteListFragment)

    fun inject(fragment: AddNoteFragment)

}
