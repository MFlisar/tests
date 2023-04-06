package com.michaelflisar.tests.tests.room.room.daos

import androidx.room.Dao
import com.michaelflisar.tests.tests.room.room.base.BaseDao
import com.michaelflisar.tests.tests.room.room.entities.WSet

@Dao
abstract class WSetDao : BaseDao.NoRef<WSet>() {

    override val classItem: Class<WSet> = WSet::class.java
    override val classItemWithRef: Class<WSet> = WSet::class.java

    override val tableName: String = "w_set"
    override val colId: String = "w_set_id"

    // ---------------
    // Flow
    // ---------------

    // ---------------
    // Sonstiges
    // ---------------

    // neuesten Sätze sind am Anfang!
    //@RewriteQueriesToDropUnusedColumns
    //@Transaction
    //@Query("select w_set.* from w_set "+
    //        "left join w_exercise on w_exercise_id = w_set.fk_exercise " +
    //        "left join w_workout on w_workout_id = w_exercise.fk_workout " +
    //        "left join w_day on w_day_id = w_workout.fk_day " +
    //        "where w_exercise.fk_exercise = :exerciseId " +
    //        "order by w_day.date desc"
    //)
    //protected abstract suspend fun loadAllSets(exerciseId: Long): List<WSet>
    //suspend fun loadAllSets(exercise: Exercise) = loadAllSets(exercise.id)

}