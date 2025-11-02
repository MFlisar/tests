package com.michaelflisar.tests.dragdrop.classes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
fun <K : Any> ReorderableCollectionItemScope.DragSelectContent(
    key: K,
    onClick: () -> Unit,
    itemCanBeDragged: Boolean = true,
    onDragStarted: (startedPosition: Offset) -> Unit = {},
    onDragStopped: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    DragSelectContent(
        key = key,
        modifier = Modifier,
        canBeDragged = itemCanBeDragged,
        onClick = onClick,
        onDragStarted = onDragStarted,
        onDragStopped = onDragStopped,
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReorderableCollectionItemScope.DragSelectContent(
    key: Any,
    canBeDragged: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDragStarted: (startedPosition: Offset) -> Unit = {},
    onDragStopped: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
        if (canBeDragged) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .fillMaxHeight()
                        .width(48.dp)
                        .draggableHandleWrapper(
                            this@DragSelectContent,
                            key,
                            onDragStarted = onDragStarted,
                            onDragStopped = onDragStopped
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DragHandle, null)
                }
            }
        }
    }
}