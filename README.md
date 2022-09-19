

# MVI

## 为什么是MVI而不是MVVM

MVVM作为流行的架构模式，应用在 Compose上，并没有大的问题或者设计缺陷。但是在使用期间，发现了并不适合我的地方，或者说是使用起来不顺手的地方：

- 数据观察者过多：如果界面有多个状态，就要多个 LiveData 或者 Flow，维护麻烦。
- 更新 UI 状态的来源过多：数据观察者多，并行或同时更新 UI,造成不必要的重绘。
- 大量订阅观察者函数，也没有约束：存储和更新没有分离，容易混乱，代码臃肿。

## 单向数据流



![img](https://developer.android.google.cn/static/images/jetpack/compose/state-unidirectional-flow.png)



> 单向数据流 (UDF) 是一种设计模式，在该模式下状态向下流动，事件向上流动。通过采用单向数据流，您可以将在界面中显示状态的可组合项与应用中存储和更改状态的部分分离开来。
>
> 使用单向数据流的应用的界面更新循环如下所示：
>
> - **事件**：界面的某一部分生成一个事件，并将其向上传递，例如将按钮点击传递给 ViewModel 进行处理；或者从应用的其他层传递事件，如指示用户会话已过期。
> - **更新状态**：事件处理脚本可能会更改状态。
> - **显示状态**：状态容器向下传递状态，界面显示此状态。

以上是[官方](https://developer.android.google.cn/jetpack/compose/architecture)对单向数据流的介绍。下面介绍适合单项数据流的架构 MVI。

## MVI

MVI 包含三部分，**Model** — **View** — **Intent**

- **Model** 表示 UI 的状态，例如加载和数据。
- **View** 根据状态展示对应 UI。
- **Intent** 代表用户与 UI 交互时的意图。例如点击一个按钮提交数据。

可以看出 MVI 完美的符合官方推荐架构 ，我们引用  [React Redux](https://react-redux.js.org/) 的概念分而治之：

- State 需要展示的状态，对应 UI 需要的数据。

- Event 来自用户和系统的是事件，也可以说是命令。

- Effect 单次状态，即不是持久状态，类似于 EventBus ,例如加载错误提示出错、或者跳转到登录页，它们只执行一次，通常在 Compose 的副作用中使用。

  

### 实现

首先我们需要约束类型的接口:

```
interface UiState

interface UiEvent

interface UiEffect
```

然后创建抽象的 ViewModel :

```
abstract class BaseViewModel< S : UiState, E : UiEvent,F : UiEffect>  : ViewModel() {}
```

对于状态的处理，我们使用`StateFlow`，`StateFlow`就像`LiveData`但具有初始值,所以需要一个初始状态。这也是一种`SharedFlow.`我们总是希望在 UI 变得可见时接收最后一个视图状态。为什么不使用`MutableState`，因为`Flow` 的api和操作符十分强大。

```

		private val initialState: S by lazy { initialState() }

    protected abstract fun initialState(): S

    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }

    val uiState: StateFlow<S> by lazy { _uiState }
```

对于意图，即事件，我要接收和处理：

```
  private val _uiEvent: MutableSharedFlow<E> = MutableSharedFlow()

    init {
        subscribeEvents()
    }

    /**
     * 收集事件
     */
    private fun subscribeEvents() {
        viewModelScope.launch {
            _uiEvent.collect {
                // reduce event
            }
        }
    }

    fun sendEvent(event: E) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
```

然后 Reducer 处理事件，更新状态：



```
    /**
     * 处理事件，更新状态
     * @param state S
     * @param event E
     */
    private fun reduceEvent(state: S, event: E) {
        viewModelScope.launch {
            handleEvent(event, state)?.let { newState -> sendState { newState } }
        }
    }
    
    protected abstract suspend fun handleEvent(event: E, state: S): S?
    
```

单一的副作用：

```
    private val _uiEffect: MutableSharedFlow<F> = MutableSharedFlow()

    val uiEffect: Flow<F> = _uiEffect
    
    protected fun sendEffect(effect: F) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
```



## 使用

接下来实现一个 Todo 应用，打开应用获取历史任务，点击加号增加一条新的任务，完成任务后后 `Toast` 提示。

首先分析都有哪些状态：

- 是否在加载历史任务

- 是否添加新任务

- 任务列表


创建约束类：
  ```
  internal data class TodoState(
      val isShowAddDialog: Boolean=false,
      val isLoading: Boolean = false,
      val todoList: List<Todo> = listOf(),
  ) : UiState
  ```



然后分析有哪些意图：

- ~~加载任务（进入自动加载，所以省略）~~
- 显示任务
- 加载框的显示隐藏
- 添加新任务
- 完成任务

```
internal sealed interface TodoEvent : UiEvent {
    data class ShowData(val items: List<Todo>) : TodoEvent
    data class OnChangeDialogState(val show: Boolean) : TodoEvent
    data class AddNewItem(val text: String) : TodoEvent
    data class OnItemCheckedChanged(val index: Int, val isChecked: Boolean) : TodoEvent
}
```

而单一事件就一种，完成任务时候的提示：

```
internal sealed interface TodoEffect : UiEffect {
    // 已完成
    data class Completed(val text: String) : TodoEffect

}
```

### 界面

```
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun TodoScreen(
    viewModel: TodoViewModel = viewModel(),
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect { effect ->
        Log.e("", "TodoScreen: collectSideEffect")
        when (effect) {
            is TodoEffect.Completed -> Toast.makeText(context,
                "${effect.text}已完成",
                Toast.LENGTH_SHORT)
                .show()
        }
    }
//    LaunchedEffect(Unit) {
//        viewModel.uiEffect.collect { effect ->
//            Log.e("", "TodoScreen: LaunchedEffect")
//
//            when (effect) {
//                is TodoEffect.Completed -> Toast.makeText(context,
//                    "${effect.text}已完成",
//                    Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
    when {
        state.isLoading -> ContentWithProgress()
        state.todoList.isNotEmpty() -> TodoListContent(
            state.todoList,
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
        Icon(imageVector = Icons.Default.Add,
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
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
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
```

### ViewModel

```
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
```

### 优化

本来单次事件在`LaunchedEffect`里加载，但是会出现在 UI 在停止状态下依然收集新事件，并且每次写`LaunchedEffect`比较麻烦，所以写了一个扩展：

```
@Composable
fun <S : UiState, E : UiEvent, F : UiEffect> BaseViewModel<S, E, F>.collectSideEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: (suspend (sideEffect: F) -> Unit),
) {
    val sideEffectFlow = this.uiEffect
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { sideEffect(it) }
        }
    }
}
```


