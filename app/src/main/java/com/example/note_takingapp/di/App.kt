package com.example.note_takingapp.di

import android.app.Application
import com.google.firebase.FirebaseApp

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    val component by lazy {
        DaggerAppComponent.create()
    }

}