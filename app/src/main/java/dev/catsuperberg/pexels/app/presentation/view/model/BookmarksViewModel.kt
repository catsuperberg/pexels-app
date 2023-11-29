package dev.catsuperberg.pexels.app.presentation.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import dev.catsuperberg.pexels.app.domain.usecase.IBookmarkedPhotoProvider
import dev.catsuperberg.pexels.app.presentation.helper.Pagination
import dev.catsuperberg.pexels.app.presentation.navigation.NavigatorCommand
import dev.catsuperberg.pexels.app.presentation.ui.destinations.DetailsScreenDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination
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

    private val pagination = Pagination(
        scope = viewModelScope,
        pageSize = 30,
        itemRequest = { page, perPage -> photoProvider.getPhotos(page, perPage) },
        onReceive = { photos -> _photos.value = (_photos.value + photos).distinctBy { it.id } }
    )

    val pageRequestAvailable: StateFlow<Boolean> = pagination.requestAvailable
    val loading: StateFlow<Boolean> = pagination.requestActive

    init {
        onRequestMorePhotos()
    }

    fun onDetails(id: Int) {
        val dbId = _photos.value[id].id
        viewModelScope.launch { _navigationEvent.emit(detailsScreenCommand(photoId = dbId)) }
    }

    fun onExplore() {
        viewModelScope.launch { _navigationEvent.emit(exploreCommand()) }
    }

    fun onRequestMorePhotos() {
        viewModelScope.launch {
            pagination.requestNextPage()
                .onFailure { Log.e(this::class.toString(), it.toString()) }
        }
    }

    private fun detailsScreenCommand(photoId: Int): NavigatorCommand = { navigator ->
        navigator.navigate(DetailsScreenDestination(DetailsScreenNavArgs(photoId = photoId, storageOnlyProvider = true)))
    }

    private fun exploreCommand(): NavigatorCommand = { navigator ->
        navigator.popBackStack(HomeScreenDestination.route, false)
    }
}
