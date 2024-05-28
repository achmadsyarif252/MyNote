package com.syarif.mynote.core.data.source.remote

import android.util.Log
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.data.UserPreferences
import com.syarif.mynote.core.data.source.remote.network.ApiService
import com.syarif.mynote.core.data.source.remote.response.AddNoteResponse
import com.syarif.mynote.core.data.source.remote.response.DeleteNoteResponse
import com.syarif.mynote.core.data.source.remote.response.EditNoteResponse
import com.syarif.mynote.core.data.source.remote.response.ListNoteResponse
import com.syarif.mynote.core.data.source.remote.response.LoginResponse
import com.syarif.mynote.core.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : MyNoteDataSource {
    override suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>> {
        return flow {
            try {
                val response = apiService.login(email, password)
                val success = response.success
                if (success == true) {
                    userPreferences.saveUser(response.data?.token ?: "", response.data?.name ?: "")
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message ?: "", response))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<RegisterResponse>> {
        return flow {
            try {
                val response = apiService.register(name, email, password)
                val success = response.success
                if (success == true) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message ?: "", response))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addNote(title: String, note: String): Flow<Resource<AddNoteResponse>> {
        return flow {
            try {
                val token = userPreferences.userToken.firstOrNull()
                if (token.isNullOrEmpty()) {
                    emit(Resource.Error("Token is null or empty"))
                    return@flow
                }

                val response = apiService.addNote(
                    "Bearer $token",
                    title,
                    note
                )
                val success = response.success
                if (success == true) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message ?: "", response))
                    Log.e("RemoteDataSource", response.message ?: "")
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getNotes(): Flow<Resource<ListNoteResponse>> {
        return flow {
            try {
                val token = userPreferences.userToken.firstOrNull()
                if (token.isNullOrEmpty()) {
                    emit(Resource.Error("Token is null or empty"))
                    return@flow
                }
                val response =
                    apiService.getNotes("Bearer $token")
                val success = response.success
                if (success == true) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message ?: "", response))
                    Log.e("RemoteDataSource", response.message ?: "")
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteNote(id: String): Flow<Resource<DeleteNoteResponse>> {
        return flow {
            try {
                val token = userPreferences.userToken.firstOrNull()
                if (token.isNullOrEmpty()) {
                    emit(Resource.Error("Token is null or empty"))
                    return@flow
                }


                val response = apiService.deleteNote(
                    "Bearer $token",
                    id
                )
                val success = response.success
                if (success == true) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message ?: "", response))
                }
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorMessage = errorResponse?.let {
                    try {
                        JSONObject(it).getString("message")
                    } catch (jsonException: Exception) {
                        "Unknown error"
                    }
                }
                emit(Resource.Error(message = errorMessage ?: e.message()))
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: "Unknown error"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateNote(
        id: String,
        title: String,
        note: String
    ): Flow<Resource<EditNoteResponse>> {
        return flow {
            try {
                val token = userPreferences.userToken.firstOrNull()
                if (token.isNullOrEmpty()) {
                    emit(Resource.Error("Token is null or empty"))
                    return@flow
                }

                val response = apiService.updateNote(
                    "Bearer $token",
                    id, title, note
                )
                val success = response.success

                if (success == true) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message ?: "", response))
                }

            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorMessage = errorResponse?.let {
                    try {
                        JSONObject(it).getString("message")
                    } catch (jsonException: Exception) {
                        "Unknown error"
                    }
                }
                emit(Resource.Error(message = errorMessage ?: e.message()))
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: "Unknown error"))
            }

        }.flowOn(Dispatchers.IO)
    }
}