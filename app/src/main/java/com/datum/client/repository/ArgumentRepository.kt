package com.datum.client.repository

object ArgumentRepository {
    private val map = mutableMapOf<Int, Any>()
    private var id: Int = 0

    fun putArgument(argument: Any): Int {
        val currentId = id++
        map[currentId] = argument
        return currentId
    }

    fun <T> getArgument(argId: Int): T {
        val obj = map[argId]
        return obj as T
    }
}