package com.example.note_takingapp.data.repository

import com.example.note_takingapp.data.dto.FirestoreNote
import com.example.note_takingapp.domain.Notes.model.Note
import com.example.note_takingapp.domain.Notes.repository.NoteRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {

    private companion object {
        const val NOTES_COLLECTION = "notes"
    }

    private val notesCollection = firestore.collection(NOTES_COLLECTION)

    override suspend fun addNote(note: Note): Result<String> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return Result.failure(Exception("User not authenticated"))

        return try {
            val firestoreNote = FirestoreNote(
                title = note.title,
                text = note.text,
                createdAt = Timestamp.now(),
                userId = currentUserId
            )
            val docRef = notesCollection.add(firestoreNote).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            notesCollection.document(noteId.toString()).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun updateNote(note: Note): Result<Unit> {
        require(note.id.isNotBlank()) { "Note ID must not be empty" }
        return try {
            val firestoreNote = FirestoreNote.fromDomain(note)
            notesCollection.document(note.id).set(firestoreNote).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNoteById(noteId: String): Result<Note> {
        return try {
            val snapshot = notesCollection.document(noteId).get().await()

            if (!snapshot.exists()) {
                return Result.failure(NoSuchElementException("Note with ID $noteId not found"))
            }

            val firestoreNote = snapshot.toObject(FirestoreNote::class.java)
                ?: return Result.failure(IllegalStateException("Failed to parse note from Firestore"))

            Result.success(firestoreNote.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




    override fun getAllNotes(): Flow<List<Note>> = callbackFlow {
        val listener = notesCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val notes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FirestoreNote::class.java)?.toDomain()
                } ?: emptyList()

                trySend(notes).isSuccess
            }

        awaitClose { listener.remove() }
    }

}