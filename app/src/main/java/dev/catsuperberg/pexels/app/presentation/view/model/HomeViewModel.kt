package dev.catsuperberg.pexels.app.presentation.view.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _searchPrompt: MutableStateFlow<String> = MutableStateFlow("")
    val searchPrompt: StateFlow<String> = _searchPrompt.asStateFlow()
    private val _searchClearAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchClearAvailable: StateFlow<Boolean> = _searchClearAvailable.asStateFlow()

    fun onSearchChange(value: String) {
        _searchPrompt.value = value
    }

    fun onSearch() {
        TODO()
    }

    fun onClearSearch() {
        TODO()
    }

    fun onDetails(id: Int) {
        TODO()
    }
}
