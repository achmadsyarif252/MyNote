package com.syarif.mynote.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class EditNoteResponse(

    @field:SerializedName("data")
    val data: EditNoteData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class EditNoteData(

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("title")
    val title: String? = null
)
