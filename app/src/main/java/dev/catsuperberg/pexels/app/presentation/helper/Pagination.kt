package dev.catsuperberg.pexels.app.presentation.helper

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Pagination<T, U: Collection<T>>(
    private val pageSize: Int,
    private val itemRequest: suspend (Int, Int) -> Result<U>,
    private val appendAction: (items: U) -> Unit
) {
    private val _reachedEmptyPage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val reachedEmptyPage: StateFlow<Boolean> = _reachedEmptyPage.asStateFlow()

    private var pagesLoaded = 0

    suspend fun requestNextPage(): Result<U> {
        Log.d("Pagination", "Requesting more photos")
        return itemRequest(pagesLoaded + 1, pageSize)
            .onSuccess { items ->
                if (items.isEmpty()) {
                    Log.d("Pagination", "Empty page")
                    _reachedEmptyPage.value = true
                    return@onSuccess
                }

                appendAction(items)
                _reachedEmptyPage.value = false
                pagesLoaded++
                Log.d("Pagination", "Added page")
            }
            .onFailure { Log.d("Pagination", it.toString()) }
    }
}
