package com.michaelflisar.tests.tests.room.room.base

import androidx.room.Dao
import com.michaelflisar.tests.tests.room.interfaces.IItem
import com.michaelflisar.tests.tests.room.interfaces.IItemWithRef
import com.michaelflisar.tests.tests.room.interfaces.daos.IDao

object BaseDao {

    @Dao
    abstract class WithRef<Item : IItem<Item>, ItemWithRef : IItemWithRef<Item>> :
        BaseBaseDao<Item, ItemWithRef>(), IDao.Ref<Item, ItemWithRef>

    @Dao
    abstract class NoRef<Item : IItem<Item>> : BaseBaseDao<Item, Item>(), IDao.NoRef<Item>

}