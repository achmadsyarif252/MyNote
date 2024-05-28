package com.syarif.mynote.ui.screens.addnote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.domain.usecase.MyNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(private val myNoteUseCase: MyNoteUseCase) : ViewModel() {

    data class AddNoteUiState(
        val isLoading: Boolean = false,
        val message: String = "",
        val isSuccess: Boolean = false
    )

    // UI State as StateFlow
    private val _uiState = MutableStateFlow(AddNoteUiState())
    val uiState: StateFlow<AddNoteUiState> = _uiState.asStateFlow()

    fun updateNote(id: String, title: String, note: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)

        myNoteUseCase.updateNote(id, title, note).collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    val result = resource.data
                    val message = if (result == true) "Note Updated Successfully" else "Failed to update note"
                    _uiState.value = _uiState.value.copy(isLoading = false, message = message, isSuccess = result == true)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, message = resource.message.toString())
                }
            }
        }
    }

    fun insert(title: String, note: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        myNoteUseCase.addNote(title, note).collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    val message = if (resource.data != null) "Note Added Successfully" else "Failed to add note"
                    _uiState.value = _uiState.value.copy(isLoading = false, message = message, isSuccess = resource.data != null)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, message = resource.message.toString())
                }
            }
        }
    }

    fun resetMessage() {
        _uiState.value = _uiState.value.copy(message = "")
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}
