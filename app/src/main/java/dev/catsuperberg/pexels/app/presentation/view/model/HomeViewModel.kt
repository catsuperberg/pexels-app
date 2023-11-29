package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.data.caching.photo.CachedPhotoRepositoryImpl
import dev.catsuperberg.pexels.app.data.helper.DataSource
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.ICollectionProvider
import dev.catsuperberg.pexels.app.presentation.exception.PaginationException.BusyException
import dev.catsuperberg.pexels.app.presentation.helper.Pagination
import dev.catsuperberg.pexels.app.presentation.helper.UiText
import dev.catsuperberg.pexels.app.presentation.navigation.NavigatorCommand
import dev.catsuperberg.pexels.app.presentation.ui.destinations.DetailsScreenDestination
import dev.catsuperberg.pexels.app.presentation.view.model.model.IPhotoMapper
import dev.catsuperberg.pexels.app.presentation.view.model.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val collectionProvider: ICollectionProvider,
    @CachedPhotoRepositoryImpl private val photoRepository: IPhotoRepository,
    private val photoMapper: IPhotoMapper
) : ViewModel() {
    enum class RequestSource { CURATED, COLLECTION, SEARCH }

    private val _navigationEvent: MutableSharedFlow<NavigatorCommand> = MutableSharedFlow()
    val navigationEvent: SharedFlow<NavigatorCommand> = _navigationEvent.asSharedFlow()

    private val _collections: MutableStateFlow<List<PexelsCollection>> = MutableStateFlow(listOf())
    val collections: StateFlow<List<String>> = _collections.map { list -> list.map { it.title } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    private val _photos: MutableStateFlow<List<PexelsPhoto>> = MutableStateFlow(listOf())
    val photos: StateFlow<List<Photo>> = _photos.map { list -> list.map(photoMapper::map)}
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val _collection: MutableStateFlow<Int?> = MutableStateFlow(null)
    val collection: StateFlow<Int?> = _collection.asStateFlow()
    private val _searchPrompt: MutableStateFlow<String> = MutableStateFlow("")

    private val collectionCount = 7
    private var collectionRequestActive: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val committedSearch: MutableStateFlow<String> = MutableStateFlow(_searchPrompt.value)
    private val committedCollection: MutableStateFlow<Int?> = MutableStateFlow(_collection.value)

    val searchPrompt: StateFlow<String> = listOf(_searchPrompt, committedSearch).merge()
        .stateIn(viewModelScope, SharingStarted.Eagerly, _searchPrompt.value)
    val requestSource: SharedFlow<RequestSource> = committedCollection.combine(committedSearch) { collection, search ->
            getSourceByPriority(collection != null, search.isNotEmpty())
        }.shareIn(viewModelScope, SharingStarted.Eagerly, 0)
    private val requestSourceState: StateFlow<RequestSource> =
        requestSource.stateIn(viewModelScope, SharingStarted.Eagerly, RequestSource.CURATED)

    private val pagination = Pagination(
        scope = viewModelScope,
        pageSize = 30,
        itemRequest = getRequestAction(RequestSource.CURATED),
        onReceive = { photos -> _photos.value = (_photos.value + photos.data).distinctBy { it.id } },
        isResultEmpty = { container -> container.data.isEmpty() },
        onClear = { _photos.value = listOf() }
    )
    val pageRequestAvailable: StateFlow<Boolean>
        get() = pagination.requestAvailable
    val loading: StateFlow<Boolean> =
        pagination.requestActive.combine(collectionRequestActive) { paginationActive, collectionActive ->
            paginationActive || collectionActive
        }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean> = _networkError.asStateFlow()

    private val _snackBarMessage: MutableSharedFlow<UiText.StringResource> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val snackBarMessage: SharedFlow<UiText.StringResource> = _snackBarMessage.asSharedFlow()

    init {
        onRequestMorePhotos()
        viewModelScope.launch { requestCollections() }
        viewModelScope.launch { _searchPrompt.debounce(700).collect { onSearch() } }
        viewModelScope.launch { requestSource.collect(::updatePagination) }
    }

    private suspend fun CoroutineScope.requestCollections() {
        collectionRequestActive.value = true
        collectionProvider.get(collectionCount)
            .onSuccess { values -> _collections.value = values }
            .onFailure { Log.e(this::class.simpleName, it.toString()) }
        collectionRequestActive.value = false
    }

    fun onSearchChange(value: String) {
        _searchPrompt.value = value
    }

    fun onSearch() {
        committedSearch.value = searchPrompt.value
        _collection.value = findCollectionByTitle(committedSearch.value)
        committedCollection.value = null
    }

    fun onClearSearch() {
        resetState()
    }

    fun onCollectionSelected(index: Int) {
        _collection.value = index
        committedCollection.value = index
        committedSearch.value = _collections.value[index].title
    }


    fun onRequestMorePhotos() {
        viewModelScope.launch {
            pagination.requestNextPage()
                .onSuccess {
                    _networkError.value = false
                    if(it.source == DataSource.DATABASE)
                        viewModelScope.launch { _snackBarMessage.emit(UiText.StringResource(R.string.data_from_cache)) }
                }
                .onFailure {
                    Log.e(this::class.simpleName, it.toString())
                    when (it) {
                        is BusyException -> { }
                        else -> {
                            if (_networkError.value.not())
                                viewModelScope.launch { _snackBarMessage.emit(UiText.StringResource(R.string.network_request_failed)) }
                            _networkError.value = true
                        }
                    }
                }
        }
    }

    fun onExplore() {
        resetState()
    }

    fun onRetry() {
        _networkError.value = false
        viewModelScope.launch { requestCollections() }
        updatePagination(requestSourceState.value)
    }

    fun onDetails(id: Int) {
        val dbId = _photos.value[id].id
        viewModelScope.launch { _navigationEvent.emit(detailsScreenCommand(photoId = dbId)) }
    }

    private fun updatePagination(source: RequestSource) {
        pagination.reset(getRequestAction(source))
        onRequestMorePhotos()
    }

    private fun resetState() {
        _searchPrompt.value = ""
        committedSearch.value = ""
        _collection.value = null
        committedCollection.value = null
    }

    private fun getSourceByPriority(collectionSelected: Boolean, searchPresent: Boolean): RequestSource = when {
        collectionSelected -> RequestSource.COLLECTION
        searchPresent -> RequestSource.SEARCH
        else -> RequestSource.CURATED
    }

    private suspend fun defaultPhotoRequest(page: Int, perPage: Int) = photoRepository.getCurated(page, perPage)

    private fun getRequestAction(source: RequestSource) = when (source) {
        RequestSource.COLLECTION -> {
            committedCollection.value?.let {
                { page, perPage -> photoRepository.getCollection(_collections.value[it].id, page, perPage) }
            } ?: ::defaultPhotoRequest
        }
        RequestSource.SEARCH -> { page, perPage -> photoRepository.getSearch(committedSearch.value, page, perPage) }
        RequestSource.CURATED -> ::defaultPhotoRequest
    }

    private fun findCollectionByTitle(title: String) = _collections.value
        .firstOrNull { it.title.equals(title, ignoreCase = true) }
        ?.let { _collections.value.indexOf(it) }

    private fun detailsScreenCommand(photoId: Int): NavigatorCommand = { navigator ->
        navigator.navigate(DetailsScreenDestination(DetailsScreenNavArgs(photoId = photoId)))
    }
}
