package com.asi.composetutorial.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun TodoScreen(
    viewModel: TodoViewModel = viewModel(),
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading -> ContentWithProgress()
        state.goodsList.isNotEmpty() -> TodoListContent(
            state.goodsList,
            state.isShowAddDialog,
            onItemCheckedChanged = { index, isChecked ->
                viewModel.sendEvent(TodoEvent.OnItemCheckedChanged(index, isChecked))
            },
            onAddButtonClick = { viewModel.sendEvent(TodoEvent.OnChangeDialogState(true)) },
            onDialogDismissClick = { viewModel.sendEvent(TodoEvent.OnChangeDialogState(false)) },
            onDialogOkClick = { text -> viewModel.sendEvent(TodoEvent.AddNewItem(text)) },
        )
    }
}

@Composable
private fun TodoListContent(
    todos: List<Todo>,
    isShowAddDialog: Boolean,
    onItemCheckedChanged: (Int, Boolean) -> Unit,
    onAddButtonClick: () -> Unit,
    onDialogDismissClick: () -> Unit,
    onDialogOkClick: (String) -> Unit,
) {
    Box {
        LazyColumn(content = {
            itemsIndexed(todos) { index, item ->
                TodoListItem(item = item, onItemCheckedChanged, index)
                if (index == todos.size - 1)
                    AddButton(onAddButtonClick)
            }

        })

        if (isShowAddDialog) {
            AddNewItemDialog(onDialogDismissClick, onDialogOkClick)
        }
    }
}

@Composable
private fun AddButton(
    onAddButtonClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(imageVector = Icons.Default.Place,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAddButtonClick
                ))
    }
}

@Composable
private fun AddNewItemDialog(
    onDialogDismissClick: () -> Unit,
    onDialogOkClick: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = { },
        text = {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Blue,
                    disabledIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Blue,
                    backgroundColor = Color.LightGray,
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { onDialogOkClick(text) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(text = "Ok", style = TextStyle(color = Color.White, fontSize = 12.sp))
            }
        }, dismissButton = {
            Button(
                onClick = onDialogDismissClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(text = "Cancel", style = TextStyle(color = Color.White, fontSize = 12.sp))
            }
        }
    )
}


@Composable
private fun TodoListItem(
    item: Todo,
    onItemCheckedChanged: (Int, Boolean) -> Unit,
    index: Int,
) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            colors = CheckboxDefaults.colors(Color.Blue),
            checked = item.isChecked,
            onCheckedChange = {
                onItemCheckedChanged(index, !item.isChecked)
            }
        )
        Text(
            text = item.text,
            modifier = Modifier.padding(start = 16.dp),
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            )
        )
    }
}


@Composable
private fun ContentWithProgress() {
    Surface(color = Color.LightGray) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}