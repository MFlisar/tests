package com.michaelflisar.tests.tests.room.enums

enum class WorkoutState(
) {
    TODO,
    IN_PROGRESS,
    UNFINISHED,
    DONE
    ;

    fun isRunning() = this == IN_PROGRESS

    fun isAtLeastStarted() = this == IN_PROGRESS || this == UNFINISHED || this == DONE
}