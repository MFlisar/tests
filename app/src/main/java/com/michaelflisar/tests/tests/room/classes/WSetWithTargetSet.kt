package com.michaelflisar.tests.tests.room.classes

import com.michaelflisar.tests.tests.room.room.entities.WSet
import com.michaelflisar.tests.tests.room.room.entities.WTargetSet

sealed class WSetWithTargetSet {

    class Set(
        var set: WSet,
        var targetSet: WTargetSet?
    ) : WSetWithTargetSet()

    class TargetOnly(
        var targetSet: WTargetSet
    ) : WSetWithTargetSet()

}