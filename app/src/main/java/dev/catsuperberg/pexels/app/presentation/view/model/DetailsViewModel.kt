package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.IBookmarkAccess
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoDownloader
import dev.catsuperberg.pexels.app.domain.usecase.ISinglePhotoProvider
import dev.catsuperberg.pexels.app.presentation.navigation.NavigatorCommand
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination
import dev.catsuperberg.pexels.app.presentation.ui.navArgs
import dev.catsuperberg.pexels.app.presentation.view.model.model.IPhotoMapper
import dev.catsuperberg.pexels.app.presentation.view.model.model.Photo
import dev.catsuperberg.pexels.app.presentation.view.model.model.PhotoQuality
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

data class DetailsScreenNavArgs(
    val photoId: Int,
    val storageOnlyProvider: Boolean = false
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val photoProvider: ISinglePhotoProvider,
    private val photoMapper: IPhotoMapper,
    private val downloader: IPhotoDownloader,
    private val bookmarkAccess: IBookmarkAccess
) : ViewModel() {
    private val navArgs: DetailsScreenNavArgs = stateHandle.navArgs()
    private val photoId = navArgs.photoId

    private val _navigationEvent: MutableSharedFlow<NavigatorCommand> = MutableSharedFlow()
    val navigationEvent: SharedFlow<NavigatorCommand> = _navigationEvent.asSharedFlow()

    private val _photo: MutableStateFlow<PexelsPhoto?> = MutableStateFlow(null)
    val photo: StateFlow<Photo?> = _photo.map { it?.let { photoMapper.map(it, PhotoQuality.ORIGINAL) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val _bookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bookmarked: StateFlow<Boolean> = _bookmarked.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _photoNotFound: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val photoNotFound: StateFlow<Boolean> = _photoNotFound.asStateFlow()

    init {
        _loading.value = true
        viewModelScope.launch {
            photoProvider.getPhoto(photoId)
                .onSuccess { value -> _photo.value = value }
                .onFailure {
                    _loading.value = false
                    _photoNotFound.value = true
                    Log.e("Details", it.toString())
                }
        }


        viewModelScope.launch { refreshBookmarked() }
    }

    fun onDownload() {
        photo.value?.let { viewModelScope.launch { downloader.download(it.url) } }
    }

    fun onBookmarkedChange() {
        viewModelScope.launch {
            bookmarkAccess.setBookmarked(photoId, !_bookmarked.value)
                .onSuccess { refreshBookmarked() }
                .onFailure { Log.e("Details", it.toString()) }
        }
    }

    fun onLoadFinished() {
        _loading.value = false
    }

    fun onExplore() {
        viewModelScope.launch { _navigationEvent.emit(exploreCommand()) }
    }

    private suspend fun refreshBookmarked() {
        bookmarkAccess.isBookmarked(photoId)
            .onSuccess { value -> _bookmarked.value = value }
            .onFailure { Log.e("Details", it.toString()) }
    }


    private fun exploreCommand(): NavigatorCommand = { navigator ->
        navigator.popBackStack(HomeScreenDestination.route, false)
    }

}
