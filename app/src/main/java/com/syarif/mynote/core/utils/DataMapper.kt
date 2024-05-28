package com.syarif.mynote.core.utils

import com.syarif.mynote.core.data.source.remote.response.Data
import com.syarif.mynote.core.data.source.remote.response.ListNoteResponse
import com.syarif.mynote.core.data.source.remote.response.NoteData
import com.syarif.mynote.core.data.source.remote.response.RegisterResponse
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.core.domain.model.Login
import com.syarif.mynote.core.domain.model.Note
import com.syarif.mynote.core.domain.model.Register

object DataMapper {
    fun mapResponseLoginToDomain(login: Data?) = Login(
        name = login?.name,
        email = login?.email,
        token = login?.token
    )

    fun mapResponseRegisterToDomain(register: RegisterResponse?) = Register(
        register?.data,
        register?.success,
        register?.message,
    )

    fun mapResponseNoteToDomain(note: NoteData?) = Note(
        note = note?.note,
        updatedAt = note?.updatedAt,
        createdAt = note?.createdAt,
        id = note?.id,
        title = note?.title
    )

    fun mapResponseListNoteToDomain(input: ListNoteResponse?) = input?.data?.map {
        ListNoteItem(
            note = it?.note,
            creator = it?.creator,
            updatedAt = it?.updatedAt,
            createdAt = it?.createdAt,
            id = it?.id,
            title = it?.title
        )
    }
}