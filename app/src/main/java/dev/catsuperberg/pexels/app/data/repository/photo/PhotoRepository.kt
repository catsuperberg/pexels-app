package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.data.exception.ApiException.EmptyAnswerException
import dev.catsuperberg.pexels.app.data.exception.ApiException.FailedRequestException
import dev.catsuperberg.pexels.app.data.model.IPhotoMapper
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
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

    override suspend fun getPhoto(id: Int): Result<PexelsPhoto> =
        withContext(Dispatchers.IO) {
            performCall { getSinglePhoto(id) }
        }

    override suspend fun getCollection(id: String, page: Int, perPage: Int): Result<List<PexelsPhoto>> =
        withContext(Dispatchers.IO) {
            performCall { getPhotos(api.getCollection(id, page, perPage)) }
        }

    private fun <T> performCall(action: () -> T) = try {
        Result.success(action())
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun getSinglePhoto(id: Int): PexelsPhoto {
        val call = api.getPhoto(id)
        val result = call.execute()
        throwOnFailedRequest(result, call)
        return result.body()?.let(mapper::map) ?: throw EmptyAnswerException("${call.request()}")
    }

    private fun <T: IMediaRequestDTO, U: Call<T>> getPhotos(call: U): List<PexelsPhoto> {
        val result = call.execute()
        throwOnFailedRequest(result, call)
        return result.body()?.media?.mapNotNull(mapper::map) ?: throw EmptyAnswerException("${call.request()}")
    }

    private fun <T, U : Call<T>> throwOnFailedRequest(result: Response<T>, call: U) {
        if (result.isSuccessful.not())
            throw FailedRequestException("${call.request()}")
    }
}
