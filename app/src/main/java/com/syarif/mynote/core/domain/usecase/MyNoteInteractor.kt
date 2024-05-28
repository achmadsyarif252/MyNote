package com.syarif.mynote.core.domain.usecase

import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.core.domain.model.Note
import com.syarif.mynote.core.domain.repository.IMyNoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyNoteInteractor @Inject constructor(private val myNoteRepository: IMyNoteRepository) :
    MyNoteUseCase {
    override suspend fun register(name: String, email: String, password: String) =
        myNoteRepository.register(name, email, password)

    override suspend fun login(email: String, password: String) =
        myNoteRepository.login(email, password)

    override suspend fun addNote(title: String, note: String): Flow<Resource<Note>> =
        myNoteRepository.addNote(title, note)

    override suspend fun getNotes(): Flow<Resource<List<ListNoteItem>?>> {
        return myNoteRepository.getNotes()
    }

    override suspend fun deleteNote(id: String): Flow<Resource<Boolean>> {
        return myNoteRepository.deleteNote(id)
    }

    override suspend fun updateNote(
        id: String,
        title: String,
        note: String
    ): Flow<Resource<Boolean>> {
        return myNoteRepository.updateNote(id, title, note)
    }


}