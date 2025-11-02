package com.michaelflisar.tests.dragdrop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.parcelize.Parcelize
import com.michaelflisar.tests.core.classes.Test
import com.michaelflisar.tests.dragdrop.classes.DragSelectContent
import com.michaelflisar.tests.dragdrop.classes.ProviderReorderableLazyListStateWrapper
import com.michaelflisar.tests.dragdrop.classes.rememberReorderableLazyListState
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Parcelize
object TestDragDrop : Test {

    override val name = "Drag Drop"

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {

        // items + setup
        val items = remember { mutableStateOf(Data.DATA) }
        val closeFoldersOnFolderDrag = true

        // Expand
        val expandedFolders = rememberSaveable { mutableStateOf(emptyList<String>()) }
        val currentDragItemIsFolder = remember { mutableStateOf(false) }
        val draggedItemKey = remember { mutableStateOf<Any?>(null) }

        // filtered items
        val calcFiltered =
            { items: List<Data.Item>, expandedFolders: List<String>, currentDragItemIsFolder: Boolean ->
                items.filter { item ->
                    when (item) {
                        is Data.Item.Folder -> true
                        is Data.Item.File -> {
                            (!closeFoldersOnFolderDrag || !currentDragItemIsFolder) &&
                                    (item.parentKey == null || expandedFolders.contains(item.parentKey))
                        }
                    }
                }
            }
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

                val itemFrom = filteredItems.value[from.index]
                val itemTo = filteredItems.value[to.index]

                val temp = items.value.toMutableList()

                var indexFromSize = 0
                if (itemFrom is Data.Item.Folder && !expandedFolders.value.contains(itemFrom.key)) {
                    indexFromSize =
                        items.value.count {
                            it is Data.Item.File && it.parentKey == itemFrom.key
                        }
                }

                var indexToAdjustment = 0
                if (itemTo is Data.Item.Folder && !expandedFolders.value.contains(itemTo.key)) {
                    indexToAdjustment =
                        items.value.count {
                            it is Data.Item.File && it.parentKey == itemTo.key
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

                items.value = temp
                filteredItems.value =
                    calcFiltered(temp, expandedFolders.value, currentDragItemIsFolder.value)
            },
            onDragStarted = { key ->
                val item = filteredItems.value.find { it.key == key }
                currentDragItemIsFolder.value = item is Data.Item.Folder
            },
            onDragStopped = {
                currentDragItemIsFolder.value = false
            },
            onDragDone = { from, to ->
                if (from != to) {
                    // persist => order in items must be transformed to the data model
                    val updated =
                        mutableListOf<Pair<Data.Item.Folder?, MutableList<Data.Item.File>>>()
                    updated += null to mutableListOf()
                    var lastFolderIndex = 0
                    for (item in items.value) {
                        when (item) {
                            is Data.Item.Folder -> {
                                updated += item to mutableListOf()
                                lastFolderIndex++
                            }

                            is Data.Item.File -> {
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
                        is Data.Item.File -> File(
                            dragDropState = dragDropState.state,
                            file = it
                        )

                        is Data.Item.Folder -> Folder(
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

@Composable
fun LazyItemScope.Folder(
    dragDropState: ReorderableLazyListState,
    folder: Data.Item.Folder,
    expandedFolders: MutableState<List<String>>,
) {
    val toggleExpand = {
        expandedFolders.value =
            if (expandedFolders.value.contains(folder.key)) {
                expandedFolders.value - folder.key
            } else {
                expandedFolders.value + folder.key
            }
    }
    ReorderableItem(
        state = dragDropState,
        key = folder.key,
        enabled = true,
    ) { isDragging ->
        DragSelectContent(
            key = folder.key,
            onClick = { toggleExpand() },
            itemCanBeDragged = true
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (expandedFolders.value.contains(folder.key)) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Folder ${folder.name} (Items: ${folder.childrenKeys.size})",
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Red
                )
            }

        }
    }
}

@Composable
fun LazyItemScope.File(
    dragDropState: ReorderableLazyListState,
    file: Data.Item.File,
) {
    ReorderableItem(
        state = dragDropState,
        key = file.key,
        enabled = true
    ) { isDragging ->
        DragSelectContent(
            key = file.key,
            onClick = { /* noop */ },
            itemCanBeDragged = true
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AudioFile,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "File ${file.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

object Data {

    sealed class Item {
        abstract val name: String
        abstract val key: String

        data class File(
            override val name: String,
            override val key: String,
            val parentKey: String?,
        ) : Item()

        data class Folder(
            override val name: String,
            override val key: String,
            val childrenKeys: List<String>,
        ) : Item()
    }

    val DATA = listOf(
        Item.File("Item 1", "item1", null),
        Item.File("Item 2", "item2", null),
        Item.Folder("Folder 1", "folder1", listOf("item3", "item4")),
        Item.File("Item 3", "item3", "folder1"),
        Item.File("Item 4", "item4", "folder1"),
        Item.Folder("Folder 2", "folder2", listOf("item5", "item6")),
        Item.File("Item 5", "item5", "folder2"),
        Item.File("Item 6", "item6", "folder2"),
    )
}
