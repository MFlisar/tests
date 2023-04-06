package com.michaelflisar.tests.tests.room.interfaces

interface IWorkoutParent<Workout : IWorkout<*, *, *>> {
    val workouts: List<Workout>
}