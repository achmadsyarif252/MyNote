package com.syarif.mynote.core.domain.model

data class EditNote(
    val data: EditData? = null,
    val success: Boolean? = null,
    val message: String? = null
)

data class EditData(
    val note: String? = null,
    val title: String? = null
)