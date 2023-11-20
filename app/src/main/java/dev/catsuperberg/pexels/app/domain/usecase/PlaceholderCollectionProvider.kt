package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO Temporary implementation without api calls
class PlaceholderCollectionProvider @Inject constructor() : ICollectionProvider {
    private val collections = listOf(
        "apple",
        "galaxy",
        "democracy",
        "algorithm",
        "photosynthesis",
        "love",
        "justice",
        "gravity",
        "creativity",
        "universe",
        "evolution",
        "language",
        "art",
        "music",
        "literature",
        "history",
        "culture",
        "science",
        "technology"
    )

    override suspend fun get(count: Int): Result<List<PexelsCollection>> = suspendCoroutine { continuation ->
        continuation.resume(
            Result.success(
                collections.subList(0, count)
                    .mapIndexed { i, value -> PexelsCollection(i.toString(), value) }
            )
        )
    }
}
