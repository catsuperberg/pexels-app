package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.data.exception.ApiException.EmptyAnswerException
import dev.catsuperberg.pexels.app.data.exception.ApiException.FailedRequestException
import dev.catsuperberg.pexels.app.data.helper.DataSource
import dev.catsuperberg.pexels.app.data.helper.SourcedContainer
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

    override suspend fun getCurated(page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> =
        withContext(Dispatchers.IO) {
            performCall { getPhotos(api.getCurated(page, perPage)) }
        }

    override suspend fun getSearch(query: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> =
        withContext(Dispatchers.IO) {
            performCall { getPhotos(api.getSearch(query, page, perPage)) }
        }

    override suspend fun getPhoto(id: Int): Result<SourcedContainer<PexelsPhoto, DataSource>> =
        withContext(Dispatchers.IO) {
            performCall { getSinglePhoto(id) }
        }

    override suspend fun getCollection(id: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> =
        withContext(Dispatchers.IO) {
            performCall { getPhotos(api.getCollection(id, page, perPage)) }
        }

    private fun <T> performCall(action: () -> T) = try {
        Result.success(action())
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun getSinglePhoto(id: Int): SourcedContainer<PexelsPhoto, DataSource> {
        val call = api.getPhoto(id)
        val result = call.execute()
        throwOnFailedRequest(result, call)
        val photo = result.body()?.let(mapper::map) ?: throw EmptyAnswerException("${call.request()}")
        return SourcedContainer(photo, DataSource.API)
    }

    private fun <T: IMediaRequestDTO, U: Call<T>> getPhotos(call: U): SourcedContainer<List<PexelsPhoto>, DataSource> {
        val result = call.execute()
        throwOnFailedRequest(result, call)
        val photos = result.body()?.media?.mapNotNull(mapper::map) ?: throw EmptyAnswerException("${call.request()}")
        return SourcedContainer(photos, DataSource.API)
    }

    private fun <T, U : Call<T>> throwOnFailedRequest(result: Response<T>, call: U) {
        if (result.isSuccessful.not())
            throw FailedRequestException("${call.request()}")
    }
}
