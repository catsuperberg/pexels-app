package dev.catsuperberg.pexels.app.data.repository.collection

import android.os.NetworkOnMainThreadException
import android.util.Log
import dev.catsuperberg.pexels.app.data.exception.DataException
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CollectionRepository @Inject constructor(
    private val api: CollectionApi,
    private val mapper: ICollectionMapper
): ICollectionRepository {
    private val maxPerPage = 80
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getFeatured(count: Int): Result<List<PexelsCollection>> = suspendCoroutine { continuation ->
        scope.launch {
            try {
                val totalResults = getTotalResults()
                if(totalResults.isFailure)
                    throw Exception("Empty body")

                totalResults.getOrNull()?.also { total ->
                    val finalCount = count.coerceAtMost(total)
                    val requestCounts = finalCount.split(maxPerPage)
                    val requestResults = requestCounts
                        .mapIndexed { page, count -> api.getFeatured(page, count).execute() }
                    val successful = requestResults.filter { it.isSuccessful }.mapNotNull { it.body() }
                    if(successful.isNotEmpty()) {
                        val collections = successful
                            .flatMap { it.collections.map(mapper::map) }
                            .distinct()
                        continuation.resume(Result.success(collections))
                    }
                    else
                        continuation.resume(Result.failure(
                            DataException.FailedRequestException("No request by ${::CollectionRepository.name} was successful")
                        ))
                }
            } catch (e: NetworkOnMainThreadException) {
                Log.e("E", "Exception in CollectionRepository: $e")
            }
        }
    }

    private fun Int.split(max: Int): List<Int> {
        if(this == 0 || max == 0)
            return listOf(0)
        val remainder = this % max
        val elements = this / max
        return if(remainder == 0) List(elements) { max } else
            List(elements + 1) { index -> if(index < elements) max else remainder }
    }

    private suspend fun getTotalResults(): Result<Int> = suspendCoroutine { continuation ->
        val result = api.getFeatured(1, 1).execute()
        if (result.isSuccessful) {
            val requestResult = result.body()
            requestResult?.also { requestDto ->
                continuation.resume(Result.success(requestDto.totalResults))
            } ?: continuation.resume(Result.failure(Exception("Empty body")))
        }
        else
            continuation.resume(Result.failure(Exception(result.errorBody().toString())))
    }
}
