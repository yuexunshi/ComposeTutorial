package com.asi.composetutorial.todo

import com.asi.mvi.contract.UiEffect
import com.asi.mvi.contract.UiEvent
import com.asi.mvi.contract.UiState


internal data class TodoState(
    val isShowAddDialog: Boolean=false,
    val isLoading: Boolean = false,
    val goodsList: List<Todo> = listOf(),
) : UiState

internal sealed interface TodoEvent : UiEvent {
    data class ShowData(val items: List<Todo>) : TodoEvent
    data class OnChangeDialogState(val show: Boolean) : TodoEvent
    data class AddNewItem(val text: String) : TodoEvent
    data class OnItemCheckedChanged(val index: Int, val isChecked: Boolean) : TodoEvent
}

internal sealed interface TodoEffect : UiEffect {
    // 已完成
    data class Completed(val text: String) : TodoEffect

}


data class Todo(
    val isChecked: Boolean,
    val text: String,
)