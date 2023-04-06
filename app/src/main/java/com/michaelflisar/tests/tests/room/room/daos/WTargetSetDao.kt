package com.michaelflisar.tests.tests.room.room.daos

import androidx.room.Dao
import com.michaelflisar.tests.tests.room.room.base.BaseDao
import com.michaelflisar.tests.tests.room.room.entities.WTargetSet

@Dao
abstract class WTargetSetDao : BaseDao.NoRef<WTargetSet>() {

    override val classItem: Class<WTargetSet> = WTargetSet::class.java
    override val classItemWithRef: Class<WTargetSet> = WTargetSet::class.java

    override val tableName: String = "w_target_set"
    override val colId: String = "w_target_set_id"

}