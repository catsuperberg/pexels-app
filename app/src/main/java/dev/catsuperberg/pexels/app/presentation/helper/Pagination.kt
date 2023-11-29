package dev.catsuperberg.pexels.app.presentation.helper

import android.util.Log
import dev.catsuperberg.pexels.app.presentation.exception.PaginationException.BusyException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class Pagination<T>(
    scope: CoroutineScope,
    private val pageSize: Int,
    private var itemRequest: suspend (Int, Int) -> Result<T>,
    private val isResultEmpty: (T) -> Boolean,
    private val onReceive: (items: T) -> Unit,
    private val onClear: () -> Unit = {}
) {
    private val _requestActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val reachedEmptyPage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val requestActive: StateFlow<Boolean> = _requestActive.asStateFlow()
    val requestAvailable: StateFlow<Boolean> = _requestActive.combine(reachedEmptyPage) { active, reachedEmpty ->
        (active || reachedEmpty).not()
    }.stateIn(scope, SharingStarted.Eagerly, true)

    private var pagesLoaded = 0

    fun reset(request: (suspend (Int, Int) -> Result<T>)? = null) {
        request?.also { itemRequest = it }
        pagesLoaded = 0
        _requestActive.value = false
        reachedEmptyPage.value = false
        onClear()
    }

    suspend fun requestNextPage(): Result<T> {
        Log.d("Pagination", "Requesting more photos")
        if (_requestActive.value)
            return Result.failure(BusyException("There is already active pagination page request"))

        _requestActive.value = true
        val result = itemRequest(pagesLoaded + 1, pageSize)
            .onSuccess { items ->
                if (isResultEmpty(items)) {
                    Log.d("Pagination", "Empty page")
                    reachedEmptyPage.value = true
                    return@onSuccess
                }

                onReceive(items)
                reachedEmptyPage.value = false
                pagesLoaded++
                Log.d("Pagination", "Added page")
            }
            .onFailure { Log.d("Pagination", it.toString()) }

        _requestActive.value = false
        return result
    }
}
