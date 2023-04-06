package com.michaelflisar.tests.tests.room.interfaces

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
interface IWorkout<Item, Exercise, Set> where Exercise : IExercise<Set>, Exercise : Item {
    val id: Long
    val items: List<Item>
    val exercises: List<Exercise>
}