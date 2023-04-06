package com.michaelflisar.tests.tests.room.interfaces

interface IItem<T: IItem<T>>: IKeyProvider {

    override val id: Long

    fun copyWithId(id: Long): T


}