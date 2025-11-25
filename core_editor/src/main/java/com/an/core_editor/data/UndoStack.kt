package com.an.core_editor.data


class UndoStack<T>(
    private val maxSize: Int = 30
) {
    private val stack = ArrayDeque<T>()

    fun push(item: T) {
        if(stack.size >= maxSize) {
            stack.removeFirst()
        }
        stack.addLast(item)

    }

    fun pop(): T?  {
        val undo = stack.removeLastOrNull()
        return undo
    }

    fun isEmpty(): Boolean = stack.isEmpty()

    fun clear() = stack.clear()
}