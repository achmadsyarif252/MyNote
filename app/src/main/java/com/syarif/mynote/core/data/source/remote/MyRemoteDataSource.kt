package com.syarif.mynote.core.data.source.remote

import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.data.source.remote.response.AddNoteResponse
import com.syarif.mynote.core.data.source.remote.response.DeleteNoteResponse
import com.syarif.mynote.core.data.source.remote.response.EditNoteResponse
import com.syarif.mynote.core.data.source.remote.response.ListNoteResponse
import com.syarif.mynote.core.data.source.remote.response.LoginResponse
import com.syarif.mynote.core.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface MyNoteDataSource {
    suspend fun register(name: String, email: String, password: String): Flow<Resource<RegisterResponse>>
    suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun addNote(title: String, note: String): Flow<Resource<AddNoteResponse>>
    suspend fun getNotes(): Flow<Resource<ListNoteResponse>>
    suspend fun deleteNote(id: String): Flow<Resource<DeleteNoteResponse>>
    suspend fun updateNote(id: String, title: String, note: String): Flow<Resource<EditNoteResponse>>
}
