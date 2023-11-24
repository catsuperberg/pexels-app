package dev.catsuperberg.pexels.app.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.BuildConfig
import dev.catsuperberg.pexels.app.data.repository.photo.Media
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoDTO
import dev.catsuperberg.pexels.app.data.repository.photo.VideoDTO
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object HttpClientModule {
    @Provides
    @OptIn(ExperimentalSerializationApi::class)
    fun providesHttpClient(): Retrofit {
        val module = SerializersModule {
            polymorphic(Media::class) {
                subclass(PhotoDTO::class)
                subclass(VideoDTO::class)
            }
        }

        val json = Json {
            namingStrategy = JsonNamingStrategy.SnakeCase
            ignoreUnknownKeys = true
            serializersModule = module
            classDiscriminator = "type"
        }

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
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
