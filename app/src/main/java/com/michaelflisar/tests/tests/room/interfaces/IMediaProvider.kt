package com.michaelflisar.tests.tests.room.interfaces

import android.widget.ImageView
import com.michaelflisar.tests.tests.room.classes.MediaData

interface IMediaProvider {
    val mediaData: String
    val media: MediaData

    fun displayImage(imageView: ImageView) :Boolean {
        return false
    }
}