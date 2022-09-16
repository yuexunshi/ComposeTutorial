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
			val goodsList = repository.getTodoList()
			sendEvent(TodoEvent.ShowData(goodsList))
		}
	}

	override fun initialState(): TodoState = TodoState(isLoading = true)

	override suspend fun handleEvent(event: TodoEvent, state: TodoState): TodoState? {
		return when (event) {
			is TodoEvent.AddNewItem -> {
				val newList = state.goodsList.toMutableList()
				newList.add(
					index = state.goodsList.size,
					element = Todo(false, event.text),
				)
				state.copy(
					goodsList = newList,
					isShowAddDialog = false
				)
			}

			is TodoEvent.OnChangeDialogState -> state.copy(
				isShowAddDialog = event.show
			)
			is TodoEvent.OnItemCheckedChanged -> {
				val newList = state.goodsList.toMutableList()
				newList[event.index] = newList[event.index].copy(isChecked = event.isChecked)
				if (event.isChecked) {
					sendEffect(TodoEffect.Completed(newList[event.index].text))
				}
				state.copy(goodsList = newList)
			}
			is TodoEvent.ShowData -> state.copy(isLoading = false, goodsList = event.items)

		}
	}
}


