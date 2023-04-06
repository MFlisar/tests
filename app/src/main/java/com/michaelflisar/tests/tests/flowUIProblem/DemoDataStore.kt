package com.michaelflisar.tests.tests.flowUIProblem

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

object DemoDataStore {

    private val stateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    val flow: Flow<Int> = stateFlow

    suspend fun update(value: Int, delayInMillis: Long = 1000) {
        withContext(Dispatchers.IO) {
            Thread.sleep(delayInMillis)
            stateFlow.emit(value)
        }
    }
}