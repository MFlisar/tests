package com.michaelflisar.tests.tests.room.interfaces.daos

import com.michaelflisar.tests.tests.room.interfaces.IItem
import com.michaelflisar.tests.tests.room.interfaces.IItemWithRef

interface IDao<ItemWithRef, Item> {

    // suspend functions
    suspend fun load(id: Long): ItemWithRef
    suspend fun tryLoad(id: Long): ItemWithRef?
    suspend fun loadAll(): List<ItemWithRef>
    suspend fun insertOrUpdate(insert: Boolean, items: List<Item>): List<Item>

    suspend fun update(item: Item)
    suspend fun insert(item : Item) : Item
    suspend fun delete(item: Item)

    suspend fun deleteAll(): Int

    suspend fun update(items: List<Item>)
    suspend fun insert(items : List<Item>) : List<Item>
    suspend fun delete(items: List<Item>)

    interface NoRef<Item : IItem<Item>> : IDao<Item, Item>
    interface Ref<Item : IItem<Item>, ItemWithRef : IItemWithRef<Item>> : IDao<ItemWithRef, Item>

    interface IDaoFlow<T> {
        // flows
        fun flowAll(): kotlinx.coroutines.flow.Flow<List<T>>
    }
}