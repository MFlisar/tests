package com.michaelflisar.tests.dragdrop.classes

sealed class Item {

    abstract val name: String
    abstract val key: String

    data class File(
        override val name: String,
        override val key: String,
        val parentKey: String?,
    ) : Item()

    data class Folder(
        override val name: String,
        override val key: String,
        val childrenKeys: List<String>,
    ) : Item()
}