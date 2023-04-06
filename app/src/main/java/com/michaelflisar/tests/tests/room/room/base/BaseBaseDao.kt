package com.michaelflisar.tests.tests.room.room.base

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.michaelflisar.tests.tests.room.interfaces.IItem
import com.michaelflisar.tests.tests.room.interfaces.daos.IDao

@Dao
abstract class BaseBaseDao<Item : IItem<Item>, ItemWithRef> : IDao<ItemWithRef, Item> {

    abstract val classItem: Class<Item>
    abstract val classItemWithRef: Class<ItemWithRef>

    fun checkClass(cls: Class<*>) = classItem == cls || classItemWithRef == cls

    abstract val tableName: String
    abstract val colId: String

    private val rawQueryLoadAll
        get() = "SELECT * FROM $tableName"
    private val rawQueryLoad
        get() = "SELECT * FROM $tableName where $colId = ?"
    private val rawQueryDeleteAll
        get() = "DELETE FROM $tableName"

    private val queryLoadAll
        get() = SimpleSQLiteQuery(rawQueryLoadAll)
    private val queryDeleteAll
        get() = SimpleSQLiteQuery(rawQueryDeleteAll)

    // ---------------
    // Suspend
    // ---------------

    //@Transaction
    //@Query("select * from ${tableName}")
    //abstract suspend fun loadAll(): List<ItemWithRef>

    @Transaction
    @RawQuery
    protected abstract suspend fun loadAll(query: SimpleSQLiteQuery): List<ItemWithRef>
    override suspend fun loadAll(): List<ItemWithRef> = loadAll(queryLoadAll)

    @Transaction
    @RawQuery
    protected abstract suspend fun load(query: SimpleSQLiteQuery): ItemWithRef?
    override suspend fun load(id: Long): ItemWithRef = load(SimpleSQLiteQuery(rawQueryLoad, arrayOf(id)))!!
    override suspend fun tryLoad(id: Long): ItemWithRef? = load(SimpleSQLiteQuery(rawQueryLoad, arrayOf(id)))

   @Transaction
   @RawQuery
   protected abstract suspend fun deleteAll(query: SimpleSQLiteQuery): Int
   override suspend fun deleteAll(): Int = deleteAll(queryDeleteAll)

    // ---------------
    // Flow
    // ---------------

    //@Transaction
    //@Query("select * from ${tableName}")
    //abstract fun flowAll(): Flow<List<ItemWithRef>>

    //@Transaction
    //@RawQuery(observedEntities = arrayOf(ItemWithRef::class))
    //protected abstract fun flowAll(query: SimpleSQLiteQuery): Flow<List<ItemWithRef>>
    //fun flowAll(): Flow<List<ItemWithRef>> = flowAll(queryLoadAll)
//
    //@Transaction
    //protected abstract fun flow(query: SimpleSQLiteQuery): Flow<ItemWithRef>
    //fun flow(id: Long): Flow<ItemWithRef> = flow(SimpleSQLiteQuery(rawQueryLoad, arrayOf(id)))

    // ---------------
    // INSERT
    // ---------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun internalInsert(items: List<Item>): List<Long>

    // https://stackoverflow.com/questions/47718872/rowid-after-insert-in-room
    //abstract fun getIdFromRowId(rowId: Long): Long

    override suspend fun insert(item: Item): Item {
        return internalInsert(listOf(item))
            .first()
            .let {
                item.copyWithId(it)
            }
    }

    override suspend fun insert(items: List<Item>): List<Item> {
        return internalInsert(items)
            .zip(items)
            .map {
                it.second.copyWithId(it.first) //getIdFromRowId(it.first)
            }
    }

    override suspend fun insertOrUpdate(insert: Boolean, items: List<Item>): List<Item> {
        if (insert) {
            return internalInsert(items)
                .zip(items)
                .map {
                    it.second.copyWithId(it.first) //getIdFromRowId(it.first)
                }
        } else {
            update(items)
            return items.toList()
        }
    }

    // ---------------
    // UPDATE
    // ---------------

    @Update
    abstract override suspend fun update(item: Item)

    @Update
    abstract override suspend fun update(items: List<Item>)

    // ---------------
    // DELETE
    // ---------------

    @Delete
    abstract override suspend fun delete(item: Item)

    @Delete
    abstract override suspend fun delete(items: List<Item>)
}