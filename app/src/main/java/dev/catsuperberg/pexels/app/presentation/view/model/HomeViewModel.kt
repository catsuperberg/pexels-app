package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.ICollectionProvider
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoProvider
import dev.catsuperberg.pexels.app.presentation.view.model.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val collectionProvider: ICollectionProvider,
    private val photoProvider: IPhotoProvider
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

    private val _photos: MutableStateFlow<List<PexelsPhoto>> = MutableStateFlow(listOf())
    val photos: StateFlow<List<Photo>> = _photos.map { list ->
        list.map {
            Photo(
                url = it.urlOptimizedSize,
                aspectRatio = it.width.toFloat() / it.height.toFloat(),
                author = it.photographer,
                description = it.alt ?: "Photo"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    init {
        viewModelScope.launch {
            collectionProvider.get(collectionCount)
                .onSuccess { values -> _collections.value = values.map { it.title } }
                .onFailure { Log.e("E", it.toString()) }
        }

        viewModelScope.launch {
            photoProvider.getCurated(30)
                .onSuccess { values -> _photos.value = values }
                .onFailure { Log.e("E", it.toString()) }
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
