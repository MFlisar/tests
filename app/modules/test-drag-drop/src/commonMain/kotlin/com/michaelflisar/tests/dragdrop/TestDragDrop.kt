package com.michaelflisar.tests.dragdrop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.parcelize.Parcelize
import com.michaelflisar.tests.core.classes.Test
import com.michaelflisar.tests.dragdrop.classes.Item
import com.michaelflisar.tests.dragdrop.classes.ProviderReorderableLazyListStateWrapper
import com.michaelflisar.tests.dragdrop.classes.rememberReorderableLazyListState
import com.michaelflisar.tests.dragdrop.composables.File
import com.michaelflisar.tests.dragdrop.composables.Folder

@Parcelize
object TestDragDrop : Test {

    val TEST_DATA = listOf(
        Item.File("Item 1", "item1", null),
        Item.File("Item 2", "item2", null),
        Item.Folder("Folder 1", "folder1", listOf("item3", "item4")),
        Item.File("Item 3", "item3", "folder1"),
        Item.File("Item 4", "item4", "folder1"),
        Item.Folder("Folder 2", "folder2", listOf("item5", "item6")),
        Item.File("Item 5", "item5", "folder2"),
        Item.File("Item 6", "item6", "folder2"),
    )

    override val name = "Drag Drop"

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {

        // settings
        val closeFoldersOnFolderDrag = true

        // items
        val items = remember { mutableStateOf(TEST_DATA) }

        // expanded state
        val expandedFolders = rememberSaveable { mutableStateOf(emptyList<String>()) }

        // drag states
        val currentDragItemIsFolder = remember { mutableStateOf(false) }
        val draggedItemKey = remember { mutableStateOf<Any?>(null) }

        // filter function
        val calcFiltered =
            { items: List<Item>, expandedFolders: List<String>, currentDragItemIsFolder: Boolean ->
                items.filter { item ->
                    when (item) {
                        is Item.Folder -> true
                        is Item.File -> {
                            (!closeFoldersOnFolderDrag || !currentDragItemIsFolder) &&
                                    (item.parentKey == null || expandedFolders.contains(item.parentKey))
                        }
                    }
                }
            }

        // filtered items
        val filteredItems = remember {
            mutableStateOf(
                calcFiltered(
                    items.value,
                    expandedFolders.value,
                    currentDragItemIsFolder.value
                )
            )
        }

        // list + drag&drop state
        val listState = rememberLazyListState()
        val dragDropState = rememberReorderableLazyListState(
            lazyListState = listState,
            scrollThresholdPadding = WindowInsets.systemBars.asPaddingValues(),
            onMove = { from, to ->

                println("onMove from ${from.index} to ${to.index}")

                // -------------------------
                // 1) logic to move items in/out of folders and so on...
                // -------------------------

                val itemFrom = filteredItems.value[from.index]
                val itemTo = filteredItems.value[to.index]

                val temp = items.value.toMutableList()

                var indexFromSize = 0
                if (itemFrom is Item.Folder && !expandedFolders.value.contains(itemFrom.key)) {
                    indexFromSize =
                        items.value.count {
                            it is Item.File && it.parentKey == itemFrom.key
                        }
                }

                var indexToAdjustment = 0
                if (itemTo is Item.Folder && !expandedFolders.value.contains(itemTo.key)) {
                    indexToAdjustment =
                        items.value.count {
                            it is Item.File && it.parentKey == itemTo.key
                        }
                }

                val indexFromInItems = temp.indexOfFirst { it.key == itemFrom.key }
                val indexToInItems = temp.indexOfFirst { it.key == itemTo.key }

                val sublist =
                    temp.subList(indexFromInItems, indexFromInItems + 1 + indexFromSize).toList()
                temp.removeAll(sublist)
                if (indexFromInItems < indexToInItems) {
                    temp.addAll(indexToInItems + indexToAdjustment - sublist.size + 1, sublist)
                } else {
                    temp.addAll(indexToInItems, sublist)
                }

                // -------------------------
                // 2) update items and filteredItems
                // INFO: items are just used to persist the order, filteredItems are used for display
                // -------------------------

                items.value = temp
                filteredItems.value =
                    calcFiltered(temp, expandedFolders.value, currentDragItemIsFolder.value)
            },
            onDragStarted = { key ->
                val item = filteredItems.value.find { it.key == key }
                currentDragItemIsFolder.value = item is Item.Folder
            },
            onDragStopped = {
                currentDragItemIsFolder.value = false
            },
            onDragDone = { from, to ->
                if (from != to) {

                    // persist => order in items must be transformed to the data model
                    val updated = mutableListOf<Pair<Item.Folder?, MutableList<Item.File>>>()
                    updated += null to mutableListOf()
                    var lastFolderIndex = 0
                    for (item in items.value) {
                        when (item) {
                            is Item.Folder -> {
                                updated += item to mutableListOf()
                                lastFolderIndex++
                            }

                            is Item.File -> {
                                updated[lastFolderIndex].second += item
                            }
                        }
                    }

                    items.value = updated.map {
                        val folder = it.first
                        val files = it.second
                        if (folder == null) {
                            files.map { it.copy(parentKey = null) }
                        } else {
                            val updatedFiles = files.map { it.copy(parentKey = folder.key) }
                            val updatedFolder =
                                folder.copy(childrenKeys = updatedFiles.map { it.key })
                            listOf(updatedFolder) + updatedFiles
                        }
                    }.flatten()

                }
            },
            draggedItemKey = draggedItemKey
        )

        val currentDragItemKey = dragDropState.draggedItemKey

        // update filtered items if necessary
        LaunchedEffect(
            items.value,
            expandedFolders.value,
            currentDragItemIsFolder.value,
            currentDragItemKey.value
        ) {
            val filtered = calcFiltered(
                items.value,
                expandedFolders.value,
                currentDragItemIsFolder.value
            )
            filteredItems.value = filtered
        }

        // LIST
        ProviderReorderableLazyListStateWrapper(
            wrapper = dragDropState
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems.value, { it.key }) {
                    when (it) {
                        is Item.File -> File(
                            dragDropState = dragDropState.state,
                            file = it
                        )

                        is Item.Folder -> Folder(
                            dragDropState = dragDropState.state,
                            folder = it,
                            expandedFolders = expandedFolders,
                        )
                    }
                }
            }
        }
    }
}
