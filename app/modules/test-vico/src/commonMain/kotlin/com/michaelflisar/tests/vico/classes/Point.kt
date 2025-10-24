package com.michaelflisar.tests.vico.classes

class Point<T>(
    val x: T,
    val y: T
) {
    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }
}