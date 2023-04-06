package com.michaelflisar.tests.tests.room.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Ignore
import com.michaelflisar.tests.tests.room.classes.MyIndex
import com.michaelflisar.tests.tests.room.classes.WSetType
import com.michaelflisar.tests.tests.room.interfaces.*

@Entity(tableName = "w_exercise")
data class WExercise(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "w_exercise_id") override val id: Long = 0L,

    // Übung
    @ColumnInfo(name = "index", defaultValue = "0") override val myIndex: MyIndex.MyDouble,
    @ColumnInfo(name = "note") val note: String,

    // Foreign Key Data
    @ColumnInfo(name = "fk_workout", index = true) val fkWorkout: Long,
    @ColumnInfo(name = "fk_exercise", index = true) val fkExercise: Long,
    @ColumnInfo(name = "fk_technique", index = true) val fkTechnique: Long

) : IItem<WExercise>, IndexDouble {

    override fun copyWithId(id: Long) = copy(id = id)
    override fun copyWithIndex(index: MyIndex.MyDouble): IIndex<*> = copy(myIndex = index)

    data class WExerciseWithRefs(

        @Embedded
        override val item: WExercise,

        // ---------------
        // To 1 Relationen
        // ---------------

        // ---------------
        // To Many Relationen
        // ---------------

        @Relation(
            parentColumn = "w_exercise_id",
            entityColumn = "fk_exercise",
            entity = WSet::class
        )
        private val _sets: MutableList<WSet>,
        @Relation(
            parentColumn = "w_exercise_id",
            entityColumn = "fk_exercise",
            entity = WTargetSet::class
        )
        private val _targetSets: MutableList<WTargetSet>

    ) : IItemWithRef<WExercise>, IndexDouble, IExercise<WSet> {

        @Ignore
        override val sets = _sets.sortedBy { it.myIndex3.value }

        override val myIndex: MyIndex.MyDouble
            get() = item.myIndex

        override fun copyWithIndex(index: MyIndex.MyDouble): IIndex<*> = copy(item = item.copy(myIndex = index))

        @Ignore
        val targetSets = _targetSets.sortedBy { it.myIndex3.value }

        fun <T : Sets.IBaseSet<T>> getSetsOfType(setType: WSetType): List<T> {
            return when (setType) {
                WSetType.Set -> sets
                WSetType.TargetSet -> targetSets
            } as List<T>
        }

        // -----------
        // PROBLEM SECTION BEGIN
        // -----------

        // WORKS!
        //fun <T : Sets.IBaseSet<T>> getSetsOfSameGroup(set: T): List<T> {

        // DOES NOT WORK!
        fun <T : Sets.IBaseSet<T>> getSetsOfSameGroup(set: Sets.IBaseSet<*>): List<T> {
            return getSetsOfType<T>(set.setType).filter { it.mainOrder == set.mainOrder }
        }

        // -----------
        // PROBLEM SECTION END
        // -----------
    }

}