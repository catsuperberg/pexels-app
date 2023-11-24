package dev.catsuperberg.pexels.app

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val imageLoader = ImageLoader.Builder(this)
            .diskCachePolicy(CachePolicy.DISABLED)
            .respectCacheHeaders(false)
            .build()

        Coil.setImageLoader(imageLoader)
    }
}
