package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.ICollectionProvider
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoProvider
import dev.catsuperberg.pexels.app.presentation.helper.Pagination
import dev.catsuperberg.pexels.app.presentation.navigation.NavigatorCommand
import dev.catsuperberg.pexels.app.presentation.ui.destinations.DetailsScreenDestination
import dev.catsuperberg.pexels.app.presentation.view.model.model.IPhotoMapper
import dev.catsuperberg.pexels.app.presentation.view.model.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val collectionProvider: ICollectionProvider,
    private val photoProvider: IPhotoProvider,
    private val photoMapper: IPhotoMapper
) : ViewModel() {
    enum class RequestSource { CURATED, COLLECTION, SEARCH }
    
    private val _navigationEvent: MutableSharedFlow<NavigatorCommand> = MutableSharedFlow()
    val navigationEvent: SharedFlow<NavigatorCommand> = _navigationEvent.asSharedFlow()

    private val _collections: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val collections: StateFlow<List<String>> = _collections.asStateFlow()
    private val _photos: MutableStateFlow<List<PexelsPhoto>> = MutableStateFlow(listOf())
    val photos: StateFlow<List<Photo>> = _photos.map { list -> list.map(photoMapper::map)}
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val _searchPrompt: MutableStateFlow<String> = MutableStateFlow("")
    val searchPrompt: StateFlow<String> = _searchPrompt.asStateFlow()
    private val _selectedCollection: MutableStateFlow<Int?> = MutableStateFlow(null)
    val selectedCollection: StateFlow<Int?> = _selectedCollection.asStateFlow()

    private val collectionCount = 7
    private var collectionRequestActive: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val committedSearch: MutableStateFlow<String> = MutableStateFlow(_searchPrompt.value)
    private val committedSelectedCollection: MutableStateFlow<Int?> = MutableStateFlow(_selectedCollection.value)
    val requestSource: SharedFlow<RequestSource> =
        committedSelectedCollection.combine(committedSearch) { collection, search ->
            if (collection != null)
                return@combine RequestSource.COLLECTION
            if (search.isNotEmpty())
                return@combine RequestSource.SEARCH
            return@combine RequestSource.CURATED
        }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    private val pagination = Pagination(
        scope = viewModelScope,
        pageSize = 30,
        itemRequest = getRequestAction(RequestSource.CURATED),
        onReceive = { photos -> _photos.value = (_photos.value + photos).distinctBy { it.id } },
        onClear = { _photos.value = listOf() }
    )
    val pageRequestAvailable: StateFlow<Boolean> = pagination.requestAvailable
    val loading: StateFlow<Boolean> = pagination.requestActive.combine(collectionRequestActive) { paginationActive, collectionActive ->
            paginationActive || collectionActive
        }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        onRequestMorePhotos()

        viewModelScope.launch { requestCollections() }
        viewModelScope.launch { requestSource.collect(::updatePagination) }
    }

    private suspend fun CoroutineScope.requestCollections() {
        collectionRequestActive.value = true
        collectionProvider.get(collectionCount)
            .onSuccess { values -> _collections.value = values.map { it.title } }
            .onFailure { Log.e(this::class.toString(), it.toString()) }
        collectionRequestActive.value = false
    }

    private fun updatePagination(source: RequestSource) {
        pagination.reset(getRequestAction(source))
        onRequestMorePhotos()
    }

    private fun getRequestAction(source: RequestSource): (suspend (Int, Int) -> Result<List<PexelsPhoto>>) {
        return when (source) {
            RequestSource.SEARCH -> { page, perPage -> photoProvider.getSearch(committedSearch.value, page, perPage) }
            else -> { page, perPage -> photoProvider.getCurated(page, perPage) }
        }
    }

    fun onSearchChange(value: String) {
        _searchPrompt.value = value
    }

    fun onSearch() {
        committedSearch.value = searchPrompt.value
    }

    fun onClearSearch() {
        _searchPrompt.value = ""
        committedSearch.value = searchPrompt.value
    }

    fun onCollectionSelected(index: Int) {
        _selectedCollection.value = index
    }

    fun onDetails(id: Int) {
        val dbId = _photos.value[id].id
        viewModelScope.launch { _navigationEvent.emit(detailsScreenCommand(photoId = dbId)) }
    }

    fun onRequestMorePhotos() {
        viewModelScope.launch {
            pagination.requestNextPage()
                .onFailure { Log.e(this::class.toString(), it.toString()) }
        }
    }

    private fun detailsScreenCommand(photoId: Int): NavigatorCommand = { navigator ->
        navigator.navigate(DetailsScreenDestination(DetailsScreenNavArgs(photoId = photoId)))
    }
}
