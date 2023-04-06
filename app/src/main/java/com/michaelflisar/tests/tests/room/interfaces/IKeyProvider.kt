package com.michaelflisar.tests.tests.room.interfaces

interface IKeyProvider {
    val id: Long
    val key: String
        get() = "${this.javaClass.name}|$id"
}