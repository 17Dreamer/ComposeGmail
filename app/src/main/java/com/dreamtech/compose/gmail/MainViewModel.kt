package com.dreamtech.compose.gmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(accountRepository: AccountRepository) : ViewModel(){

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    init{
        viewModelScope.launch {
            val isLoggedIn = accountRepository.getMainAccount() != null
            _uiState.value = MainUiState.Success(isLoggedIn = isLoggedIn)
        }
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val isLoggedIn: Boolean) : MainUiState
}