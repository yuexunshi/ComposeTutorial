package com.asi.composetutorial.todo

import kotlinx.coroutines.delay

/**
 * @ClassName TodoRepository.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月16日 10:27:00
 */
internal class TodoRepository {

     suspend fun getTodoList(): List<Todo> {
        delay(1000L)
        return listOf(
            Todo(false, "王老吉"),
            Todo(false, "红牛"),
            Todo(false, "椰汁"),
        )
    }
}