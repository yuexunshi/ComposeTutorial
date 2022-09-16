

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



