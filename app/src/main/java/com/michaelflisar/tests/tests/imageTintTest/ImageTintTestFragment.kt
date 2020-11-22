package com.michaelflisar.tests.tests.imageTintTest

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import coil.bitmap.BitmapPool
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.michaelflisar.tests.R
import com.michaelflisar.tests.databinding.FragmentImageTintTestBinding

class ImageTintTestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentImageTintTestBinding.inflate(inflater)

        val testImage = R.drawable.ic_baseline_mail_outline_24
        val backgroundUrl = "https://wallpaperaccess.com/full/220637.jpg"
        val color = Color.RED
        val mode = PorterDuff.Mode.SRC_IN

        // set background image to better visualise transparency
        val request = ImageRequest.Builder(requireContext())
                .data(backgroundUrl)
                .target {
                    binding.root.background = it
                }
                .build()
        requireContext().imageLoader.enqueue(request)

        // Variant 1
        binding.iv1.setImageResource(testImage)
        binding.iv1.colorFilter = PorterDuffColorFilter(color, mode)

        // Variant 2
        binding.iv2.setImageResource(testImage)
        binding.iv2.drawable.colorFilter = PorterDuffColorFilter(color, mode)

        // Variant 3
        binding.iv3.load(testImage) {
            transformations(ColorTintTransformation(color, mode))
        }

        // Variant 4
        Glide
                .with(this)
                .load(testImage)
                .apply(RequestOptions.bitmapTransform(jp.wasabeef.glide.transformations.ColorFilterTransformation(color)))
                .into(binding.iv4)

        return binding.root
    }

    // -------------------
    // Coil Transformation
    // -------------------

    class ColorTintTransformation(
            @ColorInt private val color: Int,
            private val mode: PorterDuff.Mode = PorterDuff.Mode.SRC_ATOP
    ) : Transformation {

        override fun key(): String = "${ColorTintTransformation::class.java.name}-$mode-$color"

        override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
            val width = input.width
            val height = input.height

            val config = input.config
            val output = pool.get(width, height, config)

            val canvas = Canvas(output)
            val paint = Paint()
            paint.isAntiAlias = true
            paint.colorFilter = PorterDuffColorFilter(color, mode)
            canvas.drawBitmap(input, 0f, 0f, paint)

            return output
        }
    }
}
