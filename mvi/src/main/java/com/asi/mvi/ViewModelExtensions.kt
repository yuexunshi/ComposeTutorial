package com.asi.mvi

/**
 * @ClassName ViewModelExtensions.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月15日 16:49:00
 */


//@Composable
//fun <S : UiState, E : UiEvent, F : UiEffect> BaseViewModel<S, E, F>.collectSideEffect(
//    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
//    sideEffect: (suspend (sideEffect: F) -> Unit),
//) {
//    val sideEffectFlow = this.uiEffect
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
////        val location by sideEffectFlow.collectAsStateWithLifecycle()
//
//        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
//            sideEffectFlow.collect { sideEffect(it) }
//        }
//    }
//}
