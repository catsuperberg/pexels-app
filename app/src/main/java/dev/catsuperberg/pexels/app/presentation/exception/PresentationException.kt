package dev.catsuperberg.pexels.app.presentation.exception

sealed class PaginationException(override val message: String) : Exception(message) {
    data class BusyException(override val message: String) : PaginationException(message)
}
