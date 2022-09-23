package com.asi.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @ClassName NavFLow.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月23日 10:46:00
 */
internal object NavFlow {
    internal object Default : RouterCommand()

    private var destinationFlow: MutableStateFlow<RouterCommand> =
        MutableStateFlow(Default)

    internal var destination = destinationFlow.asStateFlow()

    internal fun navigate(destination: RouterCommand) {
        destinationFlow.value = destination
    }
}