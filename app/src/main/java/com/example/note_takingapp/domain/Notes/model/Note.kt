package com.example.note_takingapp.domain.Notes.model

import java.util.Date

data class Note(
    val id: String,
    val title: String,
    val text: String,
    val createdAt: Date,
    val userId: String
)