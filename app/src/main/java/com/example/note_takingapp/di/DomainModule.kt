package com.example.note_takingapp.di

import com.example.note_takingapp.data.AuthRepositoryImpl
import com.example.note_takingapp.domain.Auth.repository.AuthRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindAuthRepository(impl : AuthRepositoryImpl) : AuthRepository

}
