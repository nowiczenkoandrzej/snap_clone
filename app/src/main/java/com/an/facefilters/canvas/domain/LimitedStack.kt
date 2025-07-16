package com.an.facefilters.canvas.domain

class LimitedStack<T>(
    private val maxSize: Int = 20
) {
    private val stack = ArrayDeque<T>()

    fun push(item: T) {
        if(stack.size >= maxSize) {
            stack.removeFirst()
        }
        stack.addLast(item)
    }

    fun pop(): T? = stack.removeLastOrNull()

    fun isEmpty(): Boolean = stack.isEmpty()

    fun clear() = stack.clear()
}