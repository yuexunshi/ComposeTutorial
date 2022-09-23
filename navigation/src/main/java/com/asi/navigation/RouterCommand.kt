package com.asi.navigation

import androidx.navigation.NavOptions


/**
 * @ClassName Navigator.java
 * @author usopp
 * @version 1.0.0
 * @Description 导航命令
 * @createTime 2022年09月21日 17:10:00
 */

sealed class RouterCommand {

    /**
     * 导航到指定目标
     * @property route 指定目标
     * @property options NavOptions?
     * @constructor
     */
    data class To(val route: String, val options: NavOptions? = null) :
        RouterCommand()

    data class OffAllTo(
        val route: String,
    ) : RouterCommand()

    /**
     * 返回堆栈弹出到指定目标
     * @property route 要保留的最顶层目的地
     * @property inclusive 是否也应弹出指定目标
     * @property saveState 是否应保存返回堆栈和当前目的地和路由之间的所有目的地的状态
     * @constructor
     */
    data class OffUntil(
        val route: String,
        val inclusive: Boolean,
        val saveState: Boolean = false,
    ) : RouterCommand()

    /**
     * 返回堆栈弹出当前目标，并导航到制指定目标
     * @property route String
     * @constructor
     */
    data class PopUpTo(
        val route: String,
    ) : RouterCommand()

    /**
     * 返回堆栈弹出当前目标
     */
    object Back : RouterCommand()

    /**
     * 返回堆栈弹出到指定位置,并导航到指定目标
     * @property route 指定目标
     * @property untilRoute 指定位置
     * @property inclusive 是否也应弹出指定位置
     * @property saveState 是否应保存返回堆栈和当前目的地和路由之间的所有目的地的状态
     * @constructor
     */
    data class OffUntilTo(
        val route: String,
        val untilRoute: String,
        val inclusive: Boolean,
        val saveState: Boolean = false,
    ) : RouterCommand()

}
