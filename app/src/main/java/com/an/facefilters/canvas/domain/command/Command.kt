package com.an.facefilters.canvas.domain.command

import com.an.facefilters.canvas.domain.model.Result

interface Command<T> {
    fun execute(): Result<T>
    fun undo(): Result<T>
}