package dev.catsuperberg.pexels.app.data.repository.photo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {
    @GET("v1/curated/")
    fun getCurated(@Query("page") page: Int = 1, @Query("per_page") perPage: Int = 1): Call<PhotoRequestDTO>

    @GET("v1/search/")
    fun getSearch(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 1
    ): Call<PhotoRequestDTO>


    @GET("v1/collections/{id}")
    fun getCollection(
        @Path("id") collectionId: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 1,
        @Query("type") type: String = "photos"
    ): Call<MediaCollectionRequestDTO>

    @GET("v1/photos/{id}")
    fun getPhoto(@Path("id") id: Int): Call<PhotoDTO>
}
