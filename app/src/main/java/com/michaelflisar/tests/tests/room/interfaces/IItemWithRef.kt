package com.michaelflisar.tests.tests.room.interfaces

interface IItemWithRef<Item : IItem<Item>> : IKeyProvider {

    val item: Item

    override val id: Long
        get() = item.id

    override val key: String
        get() = item.key
}