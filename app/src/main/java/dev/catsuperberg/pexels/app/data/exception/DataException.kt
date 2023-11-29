package dev.catsuperberg.pexels.app.data.exception

sealed class ApiException(override val message: String, override val cause: Throwable?) : Exception(message) {
    data class FailedRequestException(override val message: String, override val cause: Throwable? = null) :
        ApiException(message, cause)

    data class EmptyAnswerException(override val message: String, override val cause: Throwable? = null) :
        ApiException(message, cause)
}

sealed class DatabaseException(override val message: String, override val cause: Throwable?) : Exception(message) {
    data class EmptyDatabaseResponseException(override val message: String, override val cause: Throwable? = null) :
        DatabaseException(message, cause)

    data class FailedDatabaseRequestException(override val message: String, override val cause: Throwable? = null) :
        DatabaseException(message, cause)

    data class FailedDatabaseInsertException(override val message: String, override val cause: Throwable? = null) :
        DatabaseException(message, cause)

    data class FailedDatabaseDeleteException(override val message: String, override val cause: Throwable? = null) :
        DatabaseException(message, cause)
}

sealed class StorageException(override val message: String, override val cause: Throwable?) : Exception(message) {
    data class FailedSavingFileException(override val message: String, override val cause: Throwable? = null) :
        StorageException(message, cause)
}
