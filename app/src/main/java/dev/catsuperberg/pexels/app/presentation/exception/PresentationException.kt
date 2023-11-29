package dev.catsuperberg.pexels.app.presentation.exception

sealed class PaginationException(override val message: String, override val cause: Throwable?) : Exception(message) {
    data class BusyException(override val message: String, override val cause: Throwable? = null) :
        PaginationException(message, cause)
}
