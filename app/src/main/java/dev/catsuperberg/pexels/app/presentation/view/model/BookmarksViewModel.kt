package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.IBookmarkedPhotoProvider
import dev.catsuperberg.pexels.app.presentation.navigation.NavigatorCommand
import dev.catsuperberg.pexels.app.presentation.ui.destinations.DetailsScreenDestination
import dev.catsuperberg.pexels.app.presentation.view.model.model.IPhotoMapper
import dev.catsuperberg.pexels.app.presentation.view.model.model.Photo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val photoProvider: IBookmarkedPhotoProvider,
    private val photoMapper: IPhotoMapper
): ViewModel() {
    private val _navigationEvent: MutableSharedFlow<NavigatorCommand> = MutableSharedFlow()
    val navigationEvent: SharedFlow<NavigatorCommand> = _navigationEvent.asSharedFlow()

    private val _photos: MutableStateFlow<List<PexelsPhoto>> = MutableStateFlow(listOf())
    val photos: StateFlow<List<Photo>> = _photos.map { list -> list.map(photoMapper::map)}
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _reachedEmptyPage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val reachedEmptyPage: StateFlow<Boolean> = _reachedEmptyPage.asStateFlow()

    private val pageSize = 30
    private var pagesLoaded = 0
    private var requestJob: Job? = null

    init {
        onRequestMorePhotos()
    }

    fun onDetails(id: Int) {
        val dbId = _photos.value[id].id
        viewModelScope.launch { _navigationEvent.emit(detailsScreenCommand(photoId = dbId)) }
    }

    fun onRequestMorePhotos() {
        Log.d("Pagination", "Requesting more photos")
        if (updateLoadingState())
            return

        requestJob = viewModelScope.launch {
            photoProvider.getPhotos(pagesLoaded + 1, pageSize)
                .onSuccess { values ->
                    if (values.isEmpty()) {
                        Log.d("Pagination", "Empty page")
                        _reachedEmptyPage.value = true
                        return@launch
                    }

                    _reachedEmptyPage.value = false
                    _photos.value = (_photos.value + values).distinctBy {it.id }
                    pagesLoaded++
                    Log.d("Pagination", "Added page")
                }
                .onFailure { Log.d("Pagination", it.toString()) }
        }.apply { invokeOnCompletion { updateLoadingState() } }
    }

    private fun updateLoadingState(): Boolean {
        _loading.value = requestJob?.isCompleted?.not() ?: false
        return _loading.value
    }

    private fun detailsScreenCommand(photoId: Int): NavigatorCommand = { navigator ->
        navigator.navigate(DetailsScreenDestination(DetailsScreenNavArgs(photoId = photoId, storageOnlyProvider = true)))
    }
}
