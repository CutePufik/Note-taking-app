package com.example.note_takingapp.di

import com.example.note_takingapp.data.AuthRepositoryImpl
import com.example.note_takingapp.data.repository.NoteRepositoryImpl
import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import com.example.note_takingapp.domain.Notes.repository.NoteRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindAuthRepository(impl : AuthRepositoryImpl) : AuthRepository

    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl) : NoteRepository

}
