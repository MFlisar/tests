package com.michaelflisar.tests.tests.imageTintTest

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import coil.bitmap.BitmapPool
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.michaelflisar.tests.R
import com.michaelflisar.tests.base.BaseTestActivity
import com.michaelflisar.tests.databinding.TestImageTintActivityBinding

class ImageTintTestActivity : BaseTestActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = TestImageTintActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val testImage = R.drawable.ic_baseline_mail_outline_24
        val backgroundUrl = "https://wallpaperaccess.com/full/220637.jpg"
        val color = Color.WHITE
        val mode = PorterDuff.Mode.SRC_ATOP

        // set background image to better visualise transparency
        val request = ImageRequest.Builder(this)
                .data(backgroundUrl)
                .target {
                    binding.root.background = it
                }
                .build()
        imageLoader.enqueue(request)

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
    }

    // -------------------
    // Coil Transformation
    // -------------------

    class ColorTintTransformation(
            @ColorInt private val color: Int,
            private val mode: PorterDuff.Mode = PorterDuff.Mode.SRC_ATOP
    ) : Transformation {

        override fun key(): String = "5-${ColorTintTransformation::class.java.name}-$mode-$color"

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
