package dev.catsuperberg.pexels.app.data.exception

sealed class DataException {
    data class FailedRequestException(override val message: String) : Exception(message)
}
