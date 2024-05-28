package com.syarif.mynote.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.core.domain.usecase.MyNoteUseCase
import com.syarif.mynote.core.utils.Utils.filterAndSortNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val myNoteUseCase: MyNoteUseCase) : ViewModel() {

    data class HomeScreenUiState(
        val notes: List<ListNoteItem> = emptyList(),
        val isLoading: Boolean = false,
        val message: String = ""
    )

    private val _uiState = MutableStateFlow(HomeScreenUiState(isLoading = true))
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private var allNotes: List<ListNoteItem> = emptyList()

    init {
        getNotes()
    }

    fun getNotes() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, message = "")
        myNoteUseCase.getNotes().collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    allNotes = resource.data ?: emptyList()
                    _uiState.value = _uiState.value.copy(notes = allNotes, isLoading = false)
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(message = resource.message.toString(), isLoading = false)
                }
                is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true, message = "")
            }
        }
    }

    fun filterAndSortNotes(startDate: String, endDate: String) = viewModelScope.launch {
        val filteredNotes = filterAndSortNotes(allNotes, startDate, endDate)
        _uiState.value = _uiState.value.copy(notes = filteredNotes)
    }

    fun clearFilter() {
        _uiState.value = _uiState.value.copy(notes = allNotes)
    }
}


