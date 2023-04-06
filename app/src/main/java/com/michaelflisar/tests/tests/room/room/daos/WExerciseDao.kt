package com.michaelflisar.tests.tests.room.room.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.michaelflisar.tests.tests.room.interfaces.daos.IDao
import com.michaelflisar.tests.tests.room.room.base.BaseDao
import com.michaelflisar.tests.tests.room.room.entities.WExercise
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WExerciseDao : BaseDao.WithRef<WExercise, WExercise.WExerciseWithRefs>(), IDao.IDaoFlow<WExercise.WExerciseWithRefs> {

    override val classItem: Class<WExercise> = WExercise::class.java
    override val classItemWithRef: Class<WExercise.WExerciseWithRefs> = WExercise.WExerciseWithRefs::class.java

    override val tableName: String = "w_exercise"
    override val colId: String = "w_exercise_id"

    // ---------------
    // Flow
    // ---------------

    @Transaction
    @Query("select * from w_exercise")
    abstract override fun flowAll(): Flow<List<WExercise.WExerciseWithRefs>>

    // ---------------
    // Sonstiges
    // ---------------

    //@Transaction
    //@Query("select * from w_exercise " +
    //        "left join w_workout on w_workout_id = fk_workout " +
    //        "left join w_day on w_day_id = fk_day " +
    //        "where fk_exercise = :exerciseId and date < :date " +
    //        "order by date DESC, w_workout.`index` ASC, w_exercise.`index` ASC " +
    //        "LIMIT 1 OFFSET :offset"
    //)
    //protected abstract suspend fun loadPrev(exerciseId: Long, date: SimpleDate, offset: Int): List<WExercise.WExerciseWithRefs>
    //suspend fun loadPrev(exercise: Exercise, date: SimpleDate, offset: Int) = loadPrev(exercise.id, date, offset)
//
    //@Transaction
    //@Query("select count(*) from w_exercise " +
    //        "left join w_workout on w_workout_id = fk_workout " +
    //        "left join w_day on w_day_id = fk_day " +
    //        "where fk_exercise = :exerciseId and date < :date"
    //)
    //protected abstract suspend fun countPrev(exerciseId: Long, date: SimpleDate): Int
    //suspend fun countPrev(exercise: Exercise, date: SimpleDate) = countPrev(exercise.id, date)


}