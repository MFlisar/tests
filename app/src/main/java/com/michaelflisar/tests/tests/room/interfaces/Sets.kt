package com.michaelflisar.tests.tests.room.interfaces

import android.os.Parcelable
import com.michaelflisar.tests.tests.room.enums.SetGroupType
import com.michaelflisar.tests.tests.room.enums.Units
import com.michaelflisar.tests.tests.room.IOrderDoubleIndex
import com.michaelflisar.tests.tests.room.classes.WSetType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat

object Sets {

    private val WEIGHT_DECIMAL_FORMAT = DecimalFormat("0.##")

    interface IBaseSet<T : IBaseSet<T>> : IItem<T>, IOrderDoubleIndex {

        val weight: Double?
        val weightUnit: Units.Weight

        val groupType: SetGroupType

        val setType: WSetType

        fun toReadableString(withRating: Boolean): String?
        fun toReadableWeight(): String? =
            weight?.let { WEIGHT_DECIMAL_FORMAT.format(weight) + weightUnit.label }

        fun toReadableReps(): String?


        fun hasReps(): Boolean
        fun hasWeight() = weight != null

        fun copyWithGroupType(type: SetGroupType): T
        fun copyWithMainOrder(order: Int): T
        fun copyWithSubOrder(order: Int): T
        fun copyWithWeight(weight: Double?, unit: Units.Weight): T
    }

    interface ISet<T : ISet<T>> : IBaseSet<T> {
        val repsOrDuration: Int
        val done: Boolean
        override val setType: WSetType
            get() = WSetType.Set

        override fun toReadableString(withRating: Boolean): String? {
            return ""
        }

        override fun toReadableReps() = ""

        override fun hasReps() = repsOrDuration > 0

        fun toSimpleSet() = SimpleSet(weight, weightUnit, repsOrDuration)
    }

    interface ITargetSet<T : ITargetSet<T>> : IBaseSet<T> {
        val repsOrDurationFrom: Int
        val repsOrDurationTo: Int
        override val setType: WSetType
            get() = WSetType.TargetSet

        fun getBestRepsOrDuration(): Int? {
            if (repsOrDurationFrom == 0 && repsOrDurationTo == 0)
                return null
            return if (repsOrDurationFrom == repsOrDurationTo)
                repsOrDurationFrom
            else
                repsOrDurationFrom + (repsOrDurationTo - repsOrDurationFrom) / 2
        }

        private fun getReadableReps(): String? {
            if (repsOrDurationFrom == 0 && repsOrDurationTo == 0)
                return null
            return if (repsOrDurationFrom == repsOrDurationTo)
                "$repsOrDurationFrom"
            else
                "$repsOrDurationFrom-$repsOrDurationTo"
        }

        override fun toReadableString(withRating: Boolean): String? {
            val ratingInfo = ""
            val repsOrDurationInfo = getReadableReps() ?: ""
            val weightInfo = weight?.let { "${weight}${weightUnit.label}" } ?: ""

            val hasReps = repsOrDurationInfo.isNotEmpty()
            val hasWeight = weightInfo.isNotEmpty()
            val hasDefaultRepsUnit = hasReps && !hasWeight
            val hasCombiRepsUnit = hasWeight && hasReps
            val combinationSign = if (hasWeight && hasReps) " x " else ""

            val info = ""

            return info.takeIf { it.isNotEmpty() }
        }

        override fun toReadableReps(): String? {
            val repsOrDuration = getReadableReps()
            return ""
        }

        override fun hasReps() = repsOrDurationFrom > 0 || repsOrDurationTo > 0

        fun toSimpleTargetSet() =
            SimpleTargetSet(weight, weightUnit, repsOrDurationFrom, repsOrDurationTo)
    }

    @Parcelize
    data class SimpleSet(
        override val weight: Double?,
        override val weightUnit: Units.Weight,
        override val repsOrDuration: Int
    ) : ISet<SimpleSet>, Parcelable {
        @IgnoredOnParcel
        override val done = false
        @IgnoredOnParcel
        override var subOrder = -1
        @IgnoredOnParcel
        override var mainOrder = -1
        @IgnoredOnParcel
        override var groupType = SetGroupType.None
        @IgnoredOnParcel
        override val id = -1L
        override fun copyWithId(id: Long) = copy()
        override fun copyWithGroupType(type: SetGroupType) = copy()
        override fun copyWithMainOrder(order: Int) = copy()
        override fun copyWithSubOrder(order: Int) = copy()
        override fun copyWithWeight(weight: Double?, unit: Units.Weight) =
            copy(weight = weight, weightUnit = unit)

    }

    @Parcelize
    data class SimpleTargetSet(
        override val weight: Double?,
        override val weightUnit: Units.Weight,
        override val repsOrDurationFrom: Int,
        override val repsOrDurationTo: Int
    ) : ITargetSet<SimpleTargetSet>, Parcelable {
        @IgnoredOnParcel
        override var subOrder = -1
        @IgnoredOnParcel
        override var mainOrder = -1
        @IgnoredOnParcel
        override var groupType = SetGroupType.None
        @IgnoredOnParcel
        override val id = -1L
        override fun copyWithId(id: Long) = copy()
        override fun copyWithGroupType(type: SetGroupType) = copy()
        override fun copyWithMainOrder(order: Int) = copy()
        override fun copyWithSubOrder(order: Int) = copy()
        override fun copyWithWeight(weight: Double?, unit: Units.Weight) =
            copy(weight = weight, weightUnit = unit)
    }
}