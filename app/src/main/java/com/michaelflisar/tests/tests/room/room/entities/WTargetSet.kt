package com.michaelflisar.tests.tests.room.room.entities

import androidx.room.*
import com.michaelflisar.tests.tests.room.enums.SetGroupType
import com.michaelflisar.tests.tests.room.enums.Units
import com.michaelflisar.tests.tests.room.interfaces.IItem
import com.michaelflisar.tests.tests.room.interfaces.Sets

@Entity(tableName = "w_target_set")
data class WTargetSet(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "w_target_set_id") override val id: Long = 0L,

    // Set
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "weight") override val weight: Double?,
    @ColumnInfo(name = "weight_unit") override val weightUnit: Units.Weight,
    @ColumnInfo(name = "reps_or_duration_from") override val repsOrDurationFrom: Int,
    @ColumnInfo(name = "reps_or_duration_to") override val repsOrDurationTo: Int,

    // Set Group
    @ColumnInfo(name = "sub_order") override val subOrder: Int,
    @ColumnInfo(name = "group_order") override val mainOrder: Int,
    @ColumnInfo(name = "group_type") override val groupType: SetGroupType,

    // Foreign Key Data
    @ColumnInfo(name = "fk_exercise", index = true) val fkExercise: Long

) : IItem<WTargetSet>, Sets.ITargetSet<WTargetSet> {

    override fun copyWithId(id: Long) = copy(id = id)
    override fun copyWithGroupType(type: SetGroupType) = copy(groupType = type)
    override fun copyWithMainOrder(order: Int) = copy(mainOrder = order)
    override fun copyWithSubOrder(order: Int) = copy(subOrder = order)
    override fun copyWithWeight(weight: Double?, unit: Units.Weight) =
        copy(weight = weight, weightUnit = unit)
}