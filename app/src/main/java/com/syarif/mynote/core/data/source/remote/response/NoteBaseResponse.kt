package com.syarif.mynote.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NoteBaseResponse<T>(
    @field:SerializedName("data")
    val data: T? = null,
    @field:SerializedName("success")
    val success: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)