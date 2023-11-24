package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.data.exception.ApiException.EmptyAnswerException
import dev.catsuperberg.pexels.app.data.exception.ApiException.FailedRequestException
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    client: Retrofit,
    private val mapper: IPhotoMapper
) : IPhotoRepository {
    private val api = client.create(PhotoApi::class.java)

    override suspend fun getCurated(page: Int, perPage: Int): Result<List<PexelsPhoto>> =
    withContext(Dispatchers.IO) {
        performCall { getPhotos(api.getCurated(page, perPage)) }
    }

    override suspend fun getSearch(query: String, page: Int, perPage: Int): Result<List<PexelsPhoto>> =
    withContext(Dispatchers.IO) {
        performCall { getPhotos(api.getSearch(query, page, perPage)) }
    }

    override suspend fun getCollection(id: String, page: Int, perPage: Int): Result<List<PexelsPhoto>> =
    withContext(Dispatchers.IO) {
        performCall { getPhotos(api.getCollection(id, page, perPage)) }
    }

    private fun performCall(action: () -> List<PexelsPhoto>) = try {
        Result.success(action())
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun <T: IMediaRequestDTO, U: Call<T>> getPhotos(call: U): List<PexelsPhoto> {
        val result = call.execute()
        if (result.isSuccessful.not())
            throw FailedRequestException("${call.request()}")

        return result.body()?.media?.mapNotNull(mapper::map) ?: throw EmptyAnswerException("${call.request()}")
    }
}
