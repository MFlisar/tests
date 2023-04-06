package com.michaelflisar.tests.tests.room.room.managers

import android.content.Context
import androidx.room.Room
import com.michaelflisar.tests.tests.room.room.Database

internal object RoomManager {

    const val DB_NAME = "data.db"

    val DB: Database
        get() = instance!!

    @Volatile
    private var instance: Database? = null

    private fun getInstance(context: Context): Database {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, DB_NAME)
            //.addMigrations(*Migrations.ALL.toTypedArray())
            .build()
    }
}