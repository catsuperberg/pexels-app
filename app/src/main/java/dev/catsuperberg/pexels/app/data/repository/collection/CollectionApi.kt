package dev.catsuperberg.pexels.app.data.repository.collection

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CollectionApi {
    @GET("v1/collections/featured/")
    fun getFeatured(@Query("page") page: Int = 1, @Query("per_page") perPage: Int = 1): Call<CollectionRequestDTO>
}
