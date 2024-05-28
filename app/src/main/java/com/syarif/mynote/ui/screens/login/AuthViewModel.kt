package com.syarif.mynote.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syarif.mynote.core.data.Resource
import com.syarif.mynote.core.data.UserPreferences
import com.syarif.mynote.core.domain.usecase.MyNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val myNoteUseCase: MyNoteUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    // UI State Class
    data class AuthUiState(
        val isLoading: Boolean = false,
        val message: String = "",
        val loginState: Result<Unit>? = null,
        val logoutState: Result<Unit>? = null,
        val userToken: String? = null,
        val userName: String? = null
    )

    private val _uiState = MutableStateFlow(AuthUiState(isLoading = true))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userToken.collect { token ->
                _uiState.value = _uiState.value.copy(
                    userToken = token,
                    isLoading = false
                )
            }
        }
        viewModelScope.launch {
            userPreferences.userName.collect { name ->
                _uiState.value = _uiState.value.copy(userName = name)
            }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, message = "")
        try {
            myNoteUseCase.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            loginState = Result.success(Unit),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            message = "Login Failed",
                            loginState = Result.failure(Throwable("Login Failed")),
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        } catch (e: Exception) {
            _uiState.value =
                _uiState.value.copy(message = e.message ?: "Unknown error", isLoading = false)
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = runCatching {
                userPreferences.clearUser()
            }
            _uiState.value = _uiState.value.copy(logoutState = result)
        }
    }

    suspend fun readUserToken(): String? {
        return userPreferences.userTokenFlow.firstOrNull()
    }

    fun resetLoginState() {
        _uiState.value = _uiState.value.copy(loginState = null)
    }

    fun resetLogoutState() {
        _uiState.value = _uiState.value.copy(logoutState = null)
    }

    fun resetMessage() {
        _uiState.value = _uiState.value.copy(message = "")
    }
}
