package com.syarif.mynote.core.data.repository

import android.content.Context
import android.util.Log
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.data.source.remote.RemoteDataSource
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.core.domain.model.Login
import com.syarif.mynote.core.domain.model.Note
import com.syarif.mynote.core.domain.model.Register
import com.syarif.mynote.core.domain.repository.IMyNoteRepository
import com.syarif.mynote.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class MyNoteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : IMyNoteRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<Register>> {
        return remoteDataSource.register(name, email, password).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Resource.Success(DataMapper.mapResponseRegisterToDomain(resource.data))
                }

                is Resource.Error -> {
                    Resource.Error(resource.message ?: "", null)
                }

                is Resource.Loading -> {
                    Resource.Loading(null)
                }

            }
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<Login>> {
        return remoteDataSource.login(email, password).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Resource.Success(DataMapper.mapResponseLoginToDomain(resource.data?.data))
                }

                is Resource.Error -> {
                    Resource.Error(resource.message ?: "", null)
                }

                is Resource.Loading -> {
                    Resource.Loading(null)
                }
            }
        }
    }

    override suspend fun addNote(title: String, note: String): Flow<Resource<Note>> {
        return remoteDataSource.addNote(title, note).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Resource.Success(
                        DataMapper.mapResponseNoteToDomain(resource.data?.data)
                    )
                }

                is Resource.Error -> {
                    Resource.Error(resource.message ?: "", null)
                }

                is Resource.Loading -> {
                    Resource.Loading(null)
                }

            }
        }
    }

    override suspend fun getNotes(): Flow<Resource<List<ListNoteItem>?>> {
        return remoteDataSource.getNotes().map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Resource.Success(DataMapper.mapResponseListNoteToDomain(resource.data))
                }

                is Resource.Error -> {
                    Resource.Error(resource.message ?: "", null)
                }

                is Resource.Loading -> {
                    Resource.Loading(null)
                }
            }
        }

    }
    override suspend fun deleteNote(id: String): Flow<Resource<Boolean>> {
        return remoteDataSource.deleteNote(id).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Resource.Success(true)
                }

                is Resource.Error -> {
                    Resource.Error(resource.message ?: "", null)
                }

                is Resource.Loading -> {
                    Resource.Loading(null)
                }
            }
        }
    }

    override suspend fun updateNote(
        id: String,
        title: String,
        note: String
    ): Flow<Resource<Boolean>> {
        return remoteDataSource.updateNote(id, title, note).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Resource.Success(true)
                }
                is Resource.Error -> {
                    Resource.Error(resource.message ?: "", null)
                }
                is Resource.Loading -> {
                    Resource.Loading(null)
                }

            }
        }
    }
}