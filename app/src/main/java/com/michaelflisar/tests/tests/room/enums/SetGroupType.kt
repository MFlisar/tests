package com.michaelflisar.tests.tests.room.enums

enum class SetGroupType(
    val shortcut: String,
    val supportsSubSets: Boolean
) {
    None("", false),
    DropSet("D", true),
    Clusters("C", true),
    RestPause("R", true),
    WarmUp("W", false),
    CoolDown("C", false),
    CustomMultiSet("M", true)
    ;
}