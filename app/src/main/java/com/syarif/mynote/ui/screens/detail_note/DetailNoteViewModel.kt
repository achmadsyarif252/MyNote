package com.syarif.mynote.ui.screens.detail_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.core.domain.usecase.MyNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val myNoteUseCase: MyNoteUseCase
) : ViewModel() {

    data class DetailNoteUiState(
        val isLoading: Boolean = false,
        val message: String = "",
        val note: ListNoteItem? = null
    )

    private val _uiState = MutableStateFlow(DetailNoteUiState(isLoading = true))
    val uiState: StateFlow<DetailNoteUiState> = _uiState


    fun delete(
        id: String, onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, message = "")
        myNoteUseCase.deleteNote(id).collect { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is Resource.Success -> {
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            message = "Note deleted successfully"
                        )
                    onResult(true)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = resource.message.toString()
                    )
                }
            }
        }
    }

    fun getDetailNote(id: String, onSuccess: (note: ListNoteItem) -> Unit) = viewModelScope.launch {
        _uiState.value =
            _uiState.value.copy(isLoading = true, message = "")

        myNoteUseCase.getNotes().collect { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.value =
                    _uiState.value.copy(isLoading = true)

                is Resource.Success -> {
                    val note = resource.data?.find { it.id == id }
                    _uiState.value = if (note != null) {
                        _uiState.value.copy(note = note, isLoading = false)
                    } else {
                        _uiState.value.copy(message = "Note not found", isLoading = false)
                    }
                    onSuccess(note ?: ListNoteItem())
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = resource.message.toString()
                    )
                }
            }
        }
    }

    fun resetMsg() {
        _uiState.value = _uiState.value.copy(message = "")
    }
}
