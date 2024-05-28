package com.syarif.mynote.core.data.source.remote.network

import com.syarif.mynote.core.data.source.remote.response.AddNoteResponse
import com.syarif.mynote.core.data.source.remote.response.DeleteNoteResponse
import com.syarif.mynote.core.data.source.remote.response.EditNoteResponse
import com.syarif.mynote.core.data.source.remote.response.ListNoteResponse
import com.syarif.mynote.core.data.source.remote.response.LoginResponse
import com.syarif.mynote.core.data.source.remote.response.RegisterResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("note")
    suspend fun addNote(
        @Header("Authorization") token: String,
        @Field("title") title: String,
        @Field("note") note: String
    ): AddNoteResponse

    @GET("notes")
    suspend fun getNotes(
        @Header("Authorization") token: String
    ): ListNoteResponse

    @DELETE("note/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DeleteNoteResponse

    @FormUrlEncoded
    @PUT("note/{id}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("title") title: String,
        @Field("note") note: String
    ): EditNoteResponse

}