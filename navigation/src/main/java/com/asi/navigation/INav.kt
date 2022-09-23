package com.asi.navigation

import androidx.navigation.NavOptions

/**
 * @ClassName IRouter.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:40:00
 */
interface INav {
    /**
     * 导航
     * @param name String
     * @param option NavOptions?
     */
    fun to(name: String, option: NavOptions? = null)

    /**
     * 弹出当前栈并导航
     * @param name String
     */
    fun popUpTo(
        name: String,
    )

    /**
     * 返回
     */
    fun back()

    /**
     * 弹出导航栈到untilRoute并导航
     * @param route 目标路由
     * @param untilRoute String
     * @param inclusive Boolean
     * @param saveState Boolean
     */
    fun offUntilTo(
        route: String,
        untilRoute: String,
        inclusive: Boolean = false,
        saveState: Boolean = false,
    )


    /**
     * 弹出导航栈到untilRoute
     * @param untilRoute String
     * @param inclusive Boolean
     * @param saveState Boolean
     */
    fun offUntil(
        untilRoute: String,
        inclusive: Boolean = false,
        saveState: Boolean = false,
    )

    /**
     * 清空导航栈并导航
     * @param route String
     */
    fun offAllTo(route: String)

}
