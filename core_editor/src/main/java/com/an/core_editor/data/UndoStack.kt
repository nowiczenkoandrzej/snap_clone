package com.an.core_editor.data

import android.util.Log

class UndoStack<T>(
    private val maxSize: Int = 30
) {
    private val stack = ArrayDeque<T>()

    fun push(item: T) {
        if(stack.size >= maxSize) {
            stack.removeFirst()
        }
        stack.addLast(item)

        Log.d("TAG", "undo: ${stack}")

    }

    fun pop(): T?  {
        val undo = stack.removeLastOrNull()
        Log.d("TAG", "undo: ${undo}")

        return undo
    }

    fun isEmpty(): Boolean = stack.isEmpty()

    fun clear() = stack.clear()
}