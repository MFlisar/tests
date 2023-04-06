package com.michaelflisar.tests.tests.room.interfaces

interface INameable {

    companion object {

        fun getName(nameGerman: String, nameEnglish: String): String {
            return nameGerman
        }
    }

    val name: String

    interface Multi : INameable {
        val nameGerman: String
        val nameEnglish: String

        override val name: String
            get() = getName(nameGerman, nameEnglish)
    }
}