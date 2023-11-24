package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.data.exception.ApiException
import dev.catsuperberg.pexels.app.data.exception.ApiException.EmptyAnswerException
import dev.catsuperberg.pexels.app.data.exception.ApiException.FailedRequestException
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    client: Retrofit,
    private val mapper: ICollectionMapper
): ICollectionRepository {
    private val api = client.create(CollectionApi::class.java)

    private data class PageRequest(val page: Int, val perPage: Int)
    private val maxPerPage = 80

    override suspend fun getFeatured(count: Int): Result<List<PexelsCollection>> = withContext(Dispatchers.IO) {
        try {
            Result.success(getCollections(count))
        } catch (e: Exception) {
            when (e) {
                is ApiException,
                is SocketTimeoutException,
                is UnknownHostException,
                is ConnectException -> Result.failure(e)
                else -> throw e
            }
        }
    }

    private fun getCollections(count: Int): List<PexelsCollection> {
        val pageRequests = calculatePageRequests(count)
        val requestResults = pageRequests.map { api.getFeatured(it.page, it.perPage).execute() }
        val successful = requestResults.filter { it.isSuccessful }.mapNotNull { it.body() }
        if (successful.isEmpty())
            throwNoSuccessful(requestResults)

        return successful
            .flatMap { it.collections.map(mapper::map) }
            .distinct()
            .take(count)
    }

    private fun calculatePageRequests(count: Int): List<PageRequest> {
        val collectionsToReturn = count.coerceAtMost(getAvailableCount())
        val pageCount = collectionsToReturn / maxPerPage + 1
        val perPage = collectionsToReturn.coerceAtMost(maxPerPage)
        return List(pageCount) { PageRequest(it + 1, perPage) }
    }

    private fun getAvailableCount(): Int {
        val result = api.getFeatured(1, 1).execute()
        if (result.isSuccessful.not()) {
            val errorMessage = result.errorBody()?.string()
            throw FailedRequestException("Request for available collection count resulted in error: $errorMessage")
        }
        return result.body()?.totalResults ?: throw EmptyAnswerException("Total results returned by API is null")
    }

    private fun throwNoSuccessful(requestResults: List<Response<CollectionRequestDTO>>) {
        val firstFailed = requestResults.firstOrNull { it.isSuccessful.not() }
        val errorMessage = firstFailed?.let { "Error: ${it.errorBody()?.string()}." }
        throw FailedRequestException("No request by ${this::class.java.name} was successful. $errorMessage")
    }
}
