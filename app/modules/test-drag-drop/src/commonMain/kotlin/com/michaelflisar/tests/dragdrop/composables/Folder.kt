package com.michaelflisar.tests.dragdrop.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.tests.dragdrop.classes.Item
import com.michaelflisar.tests.dragdrop.classes.DragSelectContent
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.Folder(
    dragDropState: ReorderableLazyListState,
    folder: Item.Folder,
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
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Box(
                    modifier = Modifier.Companion.size(48.dp),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Icon(
                        imageVector = if (expandedFolders.value.contains(folder.key)) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.Companion.size(24.dp)
                    )
                }

                Text(
                    text = "Folder ${folder.name} (Items: ${folder.childrenKeys.size})",
                    modifier = Modifier.Companion
                        .weight(1f)
                        .minimumInteractiveComponentSize(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Companion.Red
                )
            }

        }
    }
}