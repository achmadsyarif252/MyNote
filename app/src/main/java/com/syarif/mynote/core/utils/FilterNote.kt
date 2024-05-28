package com.syarif.mynote.core.utils

import com.syarif.mynote.core.domain.model.ListNoteItem


import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun filterAndSortNotes(
        notes: List<ListNoteItem>,
        startDate: String,
        endDate: String
    ): List<ListNoteItem> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val start = dateFormat.parse(startDate)
        val end = dateFormat.parse(endDate)

        return notes.filter {
            val createdAtDate = it.createdAt?.let { dateFormat.parse(it) }
            val updatedAtDate = it.updatedAt?.let { dateFormat.parse(it) }

            (createdAtDate != null && createdAtDate in start..end) ||
                    (updatedAtDate != null && updatedAtDate in start..end)
        }.sortedBy {
            it.createdAt?.let { dateFormat.parse(it) }
        }
    }

}