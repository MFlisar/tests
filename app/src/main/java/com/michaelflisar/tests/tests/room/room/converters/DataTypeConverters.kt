package com.michaelflisar.tests.tests.room.room.converters

import androidx.room.TypeConverter
import com.michaelflisar.tests.tests.room.classes.MyIndex
import com.michaelflisar.tests.tests.room.classes.SimpleDate
import com.michaelflisar.tests.tests.room.classes.SimpleTime
import com.michaelflisar.tests.tests.room.enums.MultiSetType
import com.michaelflisar.tests.tests.room.enums.SetGroupType
import com.michaelflisar.tests.tests.room.enums.Units

class DataTypeConverters {

    companion object {
        fun <T : Enum<T>> fromEnum(enum: T) = enum.ordinal
        fun <T : Enum<T>> toEnum(ordinal: Int, values: Array<T>) = values[ordinal]
    }

    // -----------
    // Enums
    // -----------

    @TypeConverter
    fun fromWSetGroupType(enum: SetGroupType) = fromEnum(enum)

    @TypeConverter
    fun toWSetGroupType(ordinal: Int) = toEnum(ordinal, SetGroupType.values())

    @TypeConverter
    fun fromWeight(enum: Units.Weight) = fromEnum(enum)

    @TypeConverter
    fun toWeight(ordinal: Int) = toEnum(ordinal, Units.Weight.values())

    @TypeConverter
    fun fromMultiSetType(enum: MultiSetType) = fromEnum(enum)

    @TypeConverter
    fun toMultiSetType(ordinal: Int) = toEnum(ordinal, MultiSetType.values())

    // -----------
    // SimpleDate / SimpleTime
    // -----------

    @TypeConverter
    fun toSimpleDate(date: String?): SimpleDate? {
        return date?.let { SimpleDate.parseString(it) }
    }

    @TypeConverter
    fun fromSimpleDate(date: SimpleDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toSimpleTime(time: String?): SimpleTime? {
        return time?.let { SimpleTime.parseString(it, SimpleTime.Format.Database) }
    }

    @TypeConverter
    fun fromSimpleTime(time: SimpleTime?): String? {
        return time?.toString(SimpleTime.Format.Database)
    }

    // -----------
    // MediaData
    // -----------

    /*
    @TypeConverter
    fun toMediaData(data: String): MediaData {
        return MediaData(data)
    }

    @TypeConverter
    fun fromMediaData(data: MediaData): String {
        return data.mediaData
    }
     */

    // -----------
    // Index
    // -----------

    @TypeConverter
    fun fromSingleIndex(index: MyIndex.MySingle): Int = index.value

    @TypeConverter
    fun toSingleIndex(value: Int): MyIndex.MySingle = MyIndex.MySingle.create(value)

    @TypeConverter
    fun fromDoubleIndex(index: MyIndex.MyDouble): Int = index.value

    @TypeConverter
    fun toDoubleIndex(value: Int): MyIndex.MyDouble = MyIndex.MyDouble.create(value)
}