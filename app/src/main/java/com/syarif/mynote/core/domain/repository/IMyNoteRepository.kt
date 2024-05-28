package com.syarif.mynote.core.domain.repository

import android.content.Context
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.core.domain.model.Login
import com.syarif.mynote.core.domain.model.Note
import com.syarif.mynote.core.domain.model.Register
import kotlinx.coroutines.flow.Flow

interface IMyNoteRepository {
    suspend fun register(name: String, email: String, password: String): Flow<Resource<Register>>
    suspend fun login(email: String, password: String): Flow<Resource<Login>>

    suspend fun addNote(title: String, note: String): Flow<Resource<Note>>
    suspend fun getNotes(): Flow<Resource<List<ListNoteItem>?>>
    suspend fun deleteNote(id: String): Flow<Resource<Boolean>>
    suspend fun updateNote(id: String, title: String, note: String): Flow<Resource<Boolean>>

}