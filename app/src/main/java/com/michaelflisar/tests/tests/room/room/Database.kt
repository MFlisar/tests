package com.michaelflisar.tests.tests.room.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michaelflisar.tests.tests.room.room.converters.DataTypeConverters
import com.michaelflisar.tests.tests.room.room.daos.WSetDao
import com.michaelflisar.tests.tests.room.room.daos.WTargetSetDao
import com.michaelflisar.tests.tests.room.room.daos.WExerciseDao
import com.michaelflisar.tests.tests.room.room.entities.*
import com.michaelflisar.tests.tests.room.room.entities.WSet
import com.michaelflisar.tests.tests.room.room.entities.WTargetSet
import com.michaelflisar.tests.tests.room.room.entities.WExercise

@Database(
    entities = [
        WExercise::class,
        WSet::class,
        WTargetSet::class

    ],
    version = 32,
    autoMigrations = [
    ],
    exportSchema = true
)
@TypeConverters(DataTypeConverters::class)
abstract class Database : RoomDatabase() {
    // Workout
    abstract fun wExerciseDao(): WExerciseDao
    abstract fun wSetDao(): WSetDao
    abstract fun wTargetSetDao(): WTargetSetDao
}