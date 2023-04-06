package com.michaelflisar.tests.tests.room.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michaelflisar.tests.tests.room.enums.SetGroupType
import com.michaelflisar.tests.tests.room.enums.Units
import com.michaelflisar.tests.tests.room.interfaces.IItem

@Entity(tableName = "w_set")
data class WSet(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "w_set_id") override val id: Long = 0L,

    // Set
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "weight") override val weight: Double?,
    @ColumnInfo(name = "weight_unit") override val weightUnit: Units.Weight,
    @ColumnInfo(name = "reps_or_duration") override val repsOrDuration: Int,
    @ColumnInfo(name = "done") override val done: Boolean,

    // Set Group
    @ColumnInfo(name = "sub_order") override val subOrder: Int,
    @ColumnInfo(name = "group_order") override val mainOrder: Int,
    @ColumnInfo(name = "group_type") override val groupType: SetGroupType,

    // Foreign Key Data
    @ColumnInfo(name = "fk_exercise", index = true) val fkExercise: Long

) : IItem<WSet>, com.michaelflisar.tests.tests.room.interfaces.Sets.ISet<WSet> {

    override fun copyWithId(id: Long) = copy(id = id)
    override fun copyWithGroupType(type: SetGroupType) = copy(groupType = type)
    override fun copyWithMainOrder(order: Int) = copy(mainOrder = order)
    override fun copyWithSubOrder(order: Int) = copy(subOrder = order)
    override fun copyWithWeight(weight: Double?, unit: Units.Weight) =
        copy(weight = weight, weightUnit = unit)

    fun getWeightInKilos(): Double {
        if (weight == null || weight == 0.0) {
            return 0.0
        } else {
            return if (weightUnit == Units.Weight.KG) weight else (weight * 2.20462262185)
        }
    }

    fun copy(targetSet: WTargetSet, setDone: Boolean): WSet {
        var copy = this
        if (weight == null) {
            copy = copy.copy(
                weight = targetSet.weight,
                weightUnit = targetSet.weightUnit
            )

        }
        if (setDone) {
            copy = copy.copy(done = true)
        }
        return copy
    }

    fun copy(set: WSet, setDone: Boolean): WSet {
        var copy = this
        if (weight == null) {
            copy = copy.copy(
                weight = set.weight,
                weightUnit = set.weightUnit
            )
        }
        if (setDone) {
            copy = copy.copy(done = true)
        }
        return copy
    }
}