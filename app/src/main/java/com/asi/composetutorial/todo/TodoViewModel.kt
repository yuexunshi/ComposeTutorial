package com.asi.composetutorial.todo

import androidx.lifecycle.viewModelScope
import com.asi.mvi.BaseViewModel
import kotlinx.coroutines.launch

internal class TodoViewModel :
	BaseViewModel<TodoState, TodoEvent, TodoEffect>() {
	private val repository: TodoRepository = TodoRepository()

	init {
		getTodo()
	}

	private fun getTodo() {
		viewModelScope.launch {
			val todoList = repository.getTodoList()
			sendEvent(TodoEvent.ShowData(todoList))
		}
	}

	override fun initialState(): TodoState = TodoState(isLoading = true)

	override suspend fun handleEvent(event: TodoEvent, state: TodoState): TodoState? {
		return when (event) {
			is TodoEvent.AddNewItem -> {
				val newList = state.todoList.toMutableList()
				newList.add(
					index = state.todoList.size,
					element = Todo(false, event.text),
				)
				state.copy(
					todoList = newList,
					isShowAddDialog = false
				)
			}

			is TodoEvent.OnChangeDialogState -> state.copy(
				isShowAddDialog = event.show
			)
			is TodoEvent.OnItemCheckedChanged -> {
				val newList = state.todoList.toMutableList()
				newList[event.index] = newList[event.index].copy(isChecked = event.isChecked)
				if (event.isChecked) {
					sendEffect(TodoEffect.Completed(newList[event.index].text))
				}
				state.copy(todoList = newList)
			}
			is TodoEvent.ShowData -> state.copy(isLoading = false, todoList = event.items)

		}
	}
}


