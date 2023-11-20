package dev.catsuperberg.pexels.app.presentation.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.usecase.ICollectionProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val collectionProvider: ICollectionProvider
) : ViewModel() {
    private val collectionCount = 7

    private val _searchPrompt: MutableStateFlow<String> = MutableStateFlow("")
    val searchPrompt: StateFlow<String> = _searchPrompt.asStateFlow()
    private val _searchClearAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchClearAvailable: StateFlow<Boolean> = _searchClearAvailable.asStateFlow()
    private val _collections: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val collections: StateFlow<List<String>> = _collections.asStateFlow()
    private val _selectedCollection: MutableStateFlow<Int?> = MutableStateFlow(null)
    val selectedCollection: StateFlow<Int?> = _selectedCollection.asStateFlow()

    init {
        viewModelScope.launch {
            val result = collectionProvider.get(collectionCount)
            if(result.isSuccess)
                _collections.value = result.getOrNull()?.map { it.title } ?: listOf()
        }
    }

    fun onSearchChange(value: String) {
        _searchPrompt.value = value
    }

    fun onSearch() {
        TODO()
    }

    fun onClearSearch() {
        TODO()
    }

    fun onCollectionSelected(index: Int) {
        _selectedCollection.value = index
    }

    fun onDetails(id: Int) {
        TODO()
    }
}
