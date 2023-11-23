package dev.catsuperberg.pexels.app.data.exception

sealed class ApiException(override val message: String) : Exception(message) {
    data class FailedRequestException(override val message: String) : ApiException(message)
    data class EmptyAnswerException(override val message: String) : ApiException(message)
}

sealed class CacheException(override val message: String) : Exception(message) {
    data class FailedDatabaseRequestException(override val message: String) : ApiException(message)
}
