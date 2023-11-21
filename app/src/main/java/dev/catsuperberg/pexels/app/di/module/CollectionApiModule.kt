package dev.catsuperberg.pexels.app.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.BuildConfig
import dev.catsuperberg.pexels.app.data.repository.collection.CollectionApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
object CollectionApiModule {
    @Provides
    fun providesCollectionApi(): CollectionApi {
        val contentType: MediaType = MediaType.get("application/json")
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", BuildConfig.PEXELS_API_KEY)
                .build()
            chain.proceed(newRequest)
        }.build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.pexels.com/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(CollectionApi::class.java)
    }
}