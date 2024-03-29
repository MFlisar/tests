package com.michaelflisar.tests.tests.coil

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import coil.*
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.target.ViewTarget
import coil.util.CoilUtils
import coil.util.Logger
import com.google.android.material.button.MaterialButton
import com.michaelflisar.lumberjack.L
import okhttp3.OkHttpClient

object ImageManager {

    private val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, throwable: Throwable) {
            L.tag("COIL")
                    .e { "Request: ${request.data} | throwable: ${throwable.message}" }
            L.e(throwable)
        }

        override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
            L.tag("COIL").d { "Request: ${request.data} | metadata: ${metadata.dataSource} | key: ${metadata.memoryCacheKey}" }
        }
    }

    fun display(data: Any?, button: MaterialButton, builder: ImageRequest.Builder.() -> Unit = {}) {
        //button.onGlobalLayout {
        val request = ImageRequest.Builder(button.context)
                .data(data)
                .size(button.iconSize)
                .listener(listener)
                .apply(builder)
                .target(object : ViewTarget<MaterialButton> {
                    override val view = button
                    override fun onSuccess(result: Drawable) {
                        view.icon = result
                    }
                })
                .build()
        button.context.imageLoader.enqueue(request)
        //}
    }

    fun display(data: Any?, imageView: ImageView, builder: ImageRequest.Builder.() -> Unit = {}) {
        imageView.loadAny(data) {
            //crossfade(false)
            listener(listener)
            apply(builder)
        }
    }

    fun display(url: String, imageView: ImageView, builder: ImageRequest.Builder.() -> Unit = {}) {
        imageView.load(url) {
            //crossfade(false)
            listener(listener)
            apply(builder)
        }
    }

    private lateinit var imageLoader: ImageLoader

    fun init(context: Context) {
        imageLoader = ImageLoader.Builder(context)
                .crossfade(false)
                //.componentRegistry {
                //    // Fetchers
                //    add(AppIconFetcher(context))
                //    //add(AppIconFetcher2(context))
                //    // Decoders
                //    // ...
                //}
                .okHttpClient {

                    //val cacheDirectory = File(context.cacheDir, "image_cache").apply { mkdirs() }
                    //val cacheSize = 250L * 1024 * 1024 // 250MB
                    //val cache = Cache(cacheDirectory, cacheSize)

                    OkHttpClient.Builder()
                            .cache(CoilUtils.createDefaultCache(context))
                            .build()
                }
                .apply {
                    // Enable logging to the standard Android log if this is a debug build.
                    logger(object : Logger {
                        override var level = Log.VERBOSE

                        override fun log(tag: String, priority: Int, message: String?, throwable: Throwable?) {
                            L.tag("COIL").d { "[$tag] | $priority | $message" }
                            throwable?.let {
                                L.tag("COIL").e(throwable)
                            }
                        }
                    })
                }
                // Workaround wegen issue: https://github.com/coil-kt/coil/issues/567
                .bitmapPoolingEnabled(false)
                //.launchInterceptorChainOnMainThread(false)
                .allowHardware(false)
                .build()
        Coil.setImageLoader(imageLoader)
    }

    fun clearCache(context: Context, memory: Boolean = true, file: Boolean = true) {
        L.tag("COIL").d { "ImageManager init - clearing cache..." }
        // 1) clear memory cache
        if (memory) {
            val imageLoader = context.imageLoader
            imageLoader.memoryCache.clear()
        }
        // 2) clear file cache
        if (file) {
            val cache = CoilUtils.createDefaultCache(context)
            cache.directory().deleteRecursively()
        }
    }
}