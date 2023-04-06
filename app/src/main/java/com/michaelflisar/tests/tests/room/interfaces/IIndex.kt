package com.michaelflisar.tests.tests.room.interfaces

import com.michaelflisar.tests.tests.room.classes.MyIndex

//sealed interface IIndex<I: Index, T: IIndex<I, T>> {
sealed interface IIndex<I : MyIndex> {

    val myIndex: I
    fun copyWithIndex(index: I): IIndex<*>

    fun copyWithIndexUnsafe(index: MyIndex): IIndex<*> {
        return when (this) {
            is IndexDouble -> copyWithIndex(index as MyIndex.MyDouble)
            is IndexSingle -> copyWithIndex(index as MyIndex.MySingle)
        }
    }

    fun copyWithMainIndexUnsafe(index: Int): IIndex<*> {
        return when (this) {
            is IndexDouble -> this.copyWithIndex(index = this.myIndex.copy(main = index))
            is IndexSingle -> this.copyWithIndex(index = this.myIndex.copy(main = index))
        }
    }
}

interface IndexSingle : IIndex<MyIndex.MySingle>
interface IndexDouble : IIndex<MyIndex.MyDouble>