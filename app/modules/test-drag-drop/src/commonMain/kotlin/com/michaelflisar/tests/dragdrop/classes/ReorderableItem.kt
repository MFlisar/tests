package com.michaelflisar.tests.dragdrop.classes

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import sh.calvin.reorderable.DragGestureDetector
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableLazyCollectionDefaults
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

val LocalReorderableLazyListStateWrapper =
    staticCompositionLocalOf<ReorderableLazyListStateWrapper> { throw Exception("LocalReorderableLazyListState not initialised") }

@Composable
fun <T> Modifier.draggableHandleWrapper(
    scope: ReorderableCollectionItemScope,
    key: T,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onDragStarted: (startedPosition: Offset) -> Unit = {},
    onDragStopped: () -> Unit = {},
    dragGestureDetector: DragGestureDetector = DragGestureDetector.Press,
): Modifier {
    val reorderableLazyListStateWrapper = LocalReorderableLazyListStateWrapper.current
    return with(scope) {
        draggableHandle(
            enabled = enabled,
            interactionSource = interactionSource,
            onDragStarted = {
                reorderableLazyListStateWrapper.draggedItemKey.value = key
                onDragStarted(it)
            },
            onDragStopped = {
                reorderableLazyListStateWrapper.draggedItemKey.value = null
                onDragStopped()
            },
            dragGestureDetector = dragGestureDetector
        )
    }
}

class ReorderableLazyListStateWrapper(
    val state: ReorderableLazyListState,
    val draggedItemKey: MutableState<Any?>,
)

@Composable
fun ProviderReorderableLazyListStateWrapper(
    wrapper: ReorderableLazyListStateWrapper,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalReorderableLazyListStateWrapper provides wrapper
    ) {
        content()
    }
}

@Composable
fun rememberReorderableLazyListState(
    lazyListState: LazyListState,
    scrollThresholdPadding: PaddingValues = PaddingValues(0.dp),
    scrollThreshold: Dp = ReorderableLazyCollectionDefaults.ScrollThreshold,
    onMove: suspend CoroutineScope.(from: LazyListItemInfo, to: LazyListItemInfo) -> Unit,
    onDragStarted: (key: Any) -> Unit = {},
    onDragStopped: () -> Unit = {},
    onDragDone: suspend (from: Int, to: Int) -> Unit = { _, _ -> },
    draggedItemKey: MutableState<Any?> = remember { mutableStateOf<Any?>(null) }
): ReorderableLazyListStateWrapper {
    val fromFirst = rememberSaveable { mutableStateOf<Int?>(null) }
    val toLast = rememberSaveable { mutableStateOf<Int?>(null) }
    val dragDropState = rememberReorderableLazyListState(
        lazyListState = lazyListState,
        scrollThresholdPadding = scrollThresholdPadding,
        scrollThreshold = scrollThreshold,
        //scroller = scroller,
        onMove = { from, to ->
            if (fromFirst.value == null) {
                fromFirst.value = from.index
            }
            toLast.value = to.index
            onMove(from, to)
        }
    )
    LaunchedEffect(dragDropState.isAnyItemDragging) {
        val from = fromFirst.value
        val to = toLast.value
        if (!dragDropState.isAnyItemDragging) {
            fromFirst.value = null
            toLast.value = null
            if (from != null && to != null)
                onDragDone(from, to)
        }
    }
    LaunchedEffect(draggedItemKey.value) {
        val key = draggedItemKey.value
        if (key != null)
            onDragStarted(key)
        else
            onDragStopped()
    }
    return ReorderableLazyListStateWrapper(dragDropState, draggedItemKey)
}