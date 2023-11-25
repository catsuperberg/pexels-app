package dev.catsuperberg.pexels.app.presentation.view.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.catsuperberg.pexels.app.presentation.ui.navArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class DetailsScreenNavArgs(
    val photoId: Int,
)

class DetailsViewModel @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {
    private val navArgs: DetailsScreenNavArgs = state.navArgs()
    val photoId = navArgs.photoId

    private val _bookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bookmarked: StateFlow<Boolean> = _bookmarked.asStateFlow()

    fun onDownload() {
        TODO()
    }

    fun onBookmarkedChange() {
        TODO()
    }
}
