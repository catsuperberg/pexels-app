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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    init {
        viewModelScope.launch {
            photoProvider.getPhotos(1, 30)
                .onSuccess { values -> _photos.value = values }
                .onFailure { Log.e("E", it.toString()) }
        }
    }

    fun onDetails(id: Int) {
        val dbId = _photos.value[id].id
        viewModelScope.launch { _navigationEvent.emit(detailsScreenCommand(photoId = dbId)) }
    }

    private fun detailsScreenCommand(photoId: Int): NavigatorCommand = { navigator ->
        navigator.navigate(DetailsScreenDestination(DetailsScreenNavArgs(photoId = photoId, storageOnlyProvider = true)))
    }
}
