package dev.catsuperberg.pexels.app.presentation.helper

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow

@Composable
fun PaginationEffects(
    listState: LazyStaggeredGridState,
    pageRequestAvailable: State<Boolean>,
    onRequestMore: () -> Unit
) {
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { firstVisibleIndex ->
                if (pageRequestAvailable.value) {
                    val twoScreensOffset = listState.layoutInfo.visibleItemsInfo.size * 2
                    val targetIndex = listState.layoutInfo.totalItemsCount - twoScreensOffset
                    if (targetIndex < firstVisibleIndex && twoScreensOffset != 0)
                        onRequestMore()
                }
            }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.canScrollForward }.collect { if (!it) onRequestMore() }
    }
}
