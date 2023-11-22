package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.data.exception.ApiException
import dev.catsuperberg.pexels.app.data.exception.ApiException.EmptyAnswerException
import dev.catsuperberg.pexels.app.data.exception.ApiException.FailedRequestException
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val api: CollectionApi,
    private val mapper: ICollectionMapper
): ICollectionRepository {
    private val maxPerPage = 80

    override suspend fun getFeatured(count: Int): Result<List<PexelsCollection>> = withContext(Dispatchers.IO) {
        try {
            val availableCollectionCount = getTotalResults()
            Result.success(getCollections(count, availableCollectionCount))
        } catch (e: ApiException) {
            Result.failure(e)
        }
    }

    private fun getCollections(count: Int, total: Int): List<PexelsCollection> {
        val finalCount = count.coerceAtMost(total)
        return if (finalCount <= maxPerPage) getInOnePage(finalCount) else getMultiPage(count)
    }

    private fun getInOnePage(count: Int): List<PexelsCollection> {
        val result = api.getFeatured(perPage = count).execute()
        if (result.isSuccessful.not())
            throw EmptyAnswerException(result.errorBody().toString())
        return result.body()?.collections?.map(mapper::map) ?: throw EmptyAnswerException("API returned no collections")
    }

    private fun getMultiPage(count: Int): List<PexelsCollection> {
        val pageCount = count / maxPerPage
        val requestResults = List(pageCount) { api.getFeatured(perPage = maxPerPage) }.map { it.execute() }
        val successful = requestResults.filter { it.isSuccessful }.mapNotNull { it.body() }
        if (successful.isEmpty())
            throwNoSuccessful(requestResults)

        return successful
            .flatMap { it.collections.map(mapper::map) }
            .distinct()
            .take(count)
    }

    private fun throwNoSuccessful(requestResults: List<Response<CollectionRequestDTO>>) {
        val firstFailed = requestResults.firstOrNull { it.isSuccessful.not() }
        val errorMessage = firstFailed?.let { "Error: ${it.errorBody()}." } ?: ""
        throw FailedRequestException("No request by ${::CollectionRepository.name} was successful. $errorMessage")
    }

    private fun getTotalResults(): Int {
        val result = api.getFeatured(1, 1).execute()
        if (result.isSuccessful.not())
            throw EmptyAnswerException(result.errorBody().toString())
        return result.body()?.totalResults ?: throw EmptyAnswerException("Total results returned by API is null")
    }
}
