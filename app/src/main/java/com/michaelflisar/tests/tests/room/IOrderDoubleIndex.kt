package com.michaelflisar.tests.tests.room

import com.michaelflisar.tests.tests.room.classes.MyIndex

interface IOrderDoubleIndex {

    val mainOrder: Int
    val subOrder: Int

    val myIndex3 : MyIndex.MyDouble
        get() = MyIndex.MyDouble(mainOrder, subOrder)
}