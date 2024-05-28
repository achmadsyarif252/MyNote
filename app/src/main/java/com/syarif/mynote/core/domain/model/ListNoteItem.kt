package com.syarif.mynote.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ListNoteItem(
    val note: String? = null,
    val creator: String? = null,
    val updatedAt: String? = null,
    val createdAt: String? = null,
    val id: String? = null,
    var title: String? = null
): Parcelable
