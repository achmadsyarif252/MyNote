package com.syarif.mynote.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.domain.usecase.MyNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val myNoteUseCase: MyNoteUseCase) :
    ViewModel() {

    data class RegisterUiState(
        val isLoading: Boolean = false,
        val message: String = "",
        val registerState: Result<Unit>? = null
    )

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, message = "")

        myNoteUseCase.register(name, email, password).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = resource.data?.message.toString(),
                        registerState = Result.success(Unit)
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = resource.message ?: "Register Failed",
                        registerState = Result.failure(Throwable(resource.message))
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun resetMessage() {
        _uiState.value = _uiState.value.copy(message = "", registerState = null)
    }
}


