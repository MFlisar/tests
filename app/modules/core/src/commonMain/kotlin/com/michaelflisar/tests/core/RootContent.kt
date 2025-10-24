package com.michaelflisar.tests.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.michaelflisar.tests.core.classes.Device
import com.michaelflisar.tests.core.classes.Test

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RootContent(
    device: Device,
    tests: List<Test>,
) {

    val currentTest = rememberSaveable { mutableStateOf<Test?>(null) }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Tests - ${device.name}")
                            if (currentTest.value != null) {
                                Text(
                                    "Current Test: ${currentTest.value!!.name}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (currentTest.value != null) {
                            IconButton(onClick = { currentTest.value = null }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }
        ) { paddingValues ->

            // BackHandler
            BackHandler(enabled = currentTest.value != null) {
                currentTest.value = null
            }

            // Content - TestSelector or current test content
            Box(modifier = Modifier.padding(paddingValues)) {
                currentTest.value?.Content() ?: TestSelector(tests, currentTest)
            }
        }

    }
}

@Composable
private fun TestSelector(tests: List<Test>, currentTest: MutableState<Test?>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Select a test:", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(all = 8.dp),
        )

        tests.forEach { test ->
            Button(
                onClick = { currentTest.value = test },
                modifier = Modifier.fillMaxWidth().padding(all = 8.dp),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(test.name)
            }
        }
    }
}