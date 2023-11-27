package dev.catsuperberg.pexels.app.data.exception

sealed class ApiException(override val message: String) : Exception(message) {
    data class FailedRequestException(override val message: String) : ApiException(message)
    data class EmptyAnswerException(override val message: String) : ApiException(message)
}

sealed class DatabaseException(override val message: String) : Exception(message) {
    data class FailedDatabaseRequestException(override val message: String) : DatabaseException(message)
    data class FailedDatabaseInsertException(override val message: String) : DatabaseException(message)
    data class FailedDatabaseDeleteException(override val message: String) : DatabaseException(message)
}

sealed class StorageException(override val message: String) : Exception(message) {
    data class FailedSavingFileException(override val message: String) : StorageException(message)
}
