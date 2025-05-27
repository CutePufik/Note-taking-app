package com.example.note_takingapp.data.dto

import com.example.note_takingapp.domain.Notes.model.Note
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class FirestoreNote(
    @DocumentId val id: String = "",
    val title: String = "",
    val text: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val userId: String = ""
) {
    fun toDomain(): Note {
        return Note(
            id = id,
            title = title,
            text = text,
            createdAt = createdAt.toDate(),
            userId = userId
        )
    }

    companion object {
        fun fromDomain(note: Note): FirestoreNote {
            return FirestoreNote(
                id = note.id,
                title = note.title,
                text = note.text,
                createdAt = Timestamp(note.createdAt),
                userId = note.userId
            )
        }
    }
}