package com.michaelflisar.tests.dragdrop.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.tests.dragdrop.classes.DragSelectContent
import com.michaelflisar.tests.dragdrop.classes.Item
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.File(
    dragDropState: ReorderableLazyListState,
    file: Item.File,
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
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Box(
                    modifier = Modifier.Companion.size(48.dp),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AudioFile,
                        contentDescription = null,
                        modifier = Modifier.Companion.size(24.dp)
                    )
                }
                Text(
                    text = "File ${file.name}",
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}