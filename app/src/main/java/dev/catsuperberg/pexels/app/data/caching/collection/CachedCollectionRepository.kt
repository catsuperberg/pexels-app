package dev.catsuperberg.pexels.app.data.caching.collection

import android.util.Log
import dev.catsuperberg.pexels.app.data.caching.CacheConst
import dev.catsuperberg.pexels.app.data.exception.DatabaseException.FailedDatabaseRequestException
import dev.catsuperberg.pexels.app.data.model.ICollectionMapper
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject

class CachedCollectionRepository @Inject constructor(
    private val repository: ICollectionRepository,
    private val collectionDao: CollectionDao,
    private val mapper: ICollectionMapper
): ICollectionRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getFeatured(count: Int): Result<List<PexelsCollection>> = withContext(Dispatchers.IO) {
        val result = repository.getFeatured(count)
        result.fold(
            onSuccess = {
                updateDbCollections(it)
                result
            },
            onFailure = { getFromDb(count) }
        )
    }

    private fun updateDbCollections(collections: List<PexelsCollection>) {
        try {
            collectionDao.deleteAll()
            val now = DateTime.now(DateTimeZone.UTC).millis
            val entities = collections.map { mapper.map(it, now) }
            collectionDao.insertAll(*entities.toTypedArray())
        } catch (e: Exception) {
            Log.e("Caching", "Failed updating collection db: $e")
        }
    }

    private fun getFromDb(count: Int): Result<List<PexelsCollection>> {
        return try {
            val entities = clearExpired(collectionDao.getFirst(count))
            Result.success(entities.map(mapper::map))
        } catch (e: Exception) {
            Result.failure(FailedDatabaseRequestException(e.toString()))
        }
    }

    private fun clearExpired(entities: List<CollectionEntity>): List<CollectionEntity> {
        val expiredBefore = DateTime.now(DateTimeZone.UTC).minusSeconds(CacheConst.expirationSeconds).millis
        val (valid, expired) = entities.partition { it.timeAdded > expiredBefore }
        scope.launch { expired.forEach(collectionDao::delete) }
        return valid
    }
}
