package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.IBookmarkAccess
import dev.catsuperberg.pexels.app.domain.usecase.ISinglePhotoProvider
import dev.catsuperberg.pexels.app.presentation.ui.navArgs
import dev.catsuperberg.pexels.app.presentation.view.model.model.IPhotoMapper
import dev.catsuperberg.pexels.app.presentation.view.model.model.Photo
import dev.catsuperberg.pexels.app.presentation.view.model.model.PhotoQuality
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsScreenNavArgs(
    val photoId: Int,
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val photoProvider: ISinglePhotoProvider,
    private val photoMapper: IPhotoMapper,
    private val bookmarkAccess: IBookmarkAccess
) : ViewModel() {
    private val navArgs: DetailsScreenNavArgs = state.navArgs()
    private val photoId = navArgs.photoId

    private val _photo: MutableStateFlow<PexelsPhoto?> = MutableStateFlow(null)
    val photo: StateFlow<Photo?> = _photo.map { it?.let { photoMapper.map(it, PhotoQuality.ORIGINAL) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val _bookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bookmarked: StateFlow<Boolean> = _bookmarked.asStateFlow()

    init {
        viewModelScope.launch {
            photoProvider.getPhoto(photoId)
                .onSuccess { value -> _photo.value = value }
                .onFailure { Log.e("E", it.toString()) }
        }


        viewModelScope.launch { refreshBookmarked() }
    }

    fun onDownload() {
        TODO()
    }

    fun onBookmarkedChange() {
        viewModelScope.launch {
            bookmarkAccess.setBookmarked(photoId, !_bookmarked.value)
                .onSuccess { refreshBookmarked() }
                .onFailure { Log.e("E", it.toString()) }
        }
    }

    private suspend fun refreshBookmarked() {
        bookmarkAccess.isBookmarked(photoId)
            .onSuccess { value -> _bookmarked.value = value }
            .onFailure { Log.e("E", it.toString()) }
    }
}
