package com.michaelflisar.tests.tests.room.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaData : Parcelable {

    @Parcelize
    object Empty : MediaData()

    @Parcelize
    class Link(
        val url: String
    ) : MediaData()
}