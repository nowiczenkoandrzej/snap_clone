package com.an.facefilters.canvas.domain.managers

import com.an.facefilters.canvas.domain.command.Command
import com.an.facefilters.canvas.domain.model.Result

class CommandManager<T> {
    private val undoStack = ArrayDeque<Command<T>>()
    private val redoStack = ArrayDeque<Command<T>>()

    fun execute(command: Command<T>): Result<T> {
        val result = command.execute()
        return when(result) {
            is Result.Success -> {
                undoStack.addLast(command)
                redoStack.clear()
                result
            }
            is Result.Failure -> result
        }
    }

    fun undo(): Result<T> {
        val command = undoStack.removeLastOrNull() ?: return Result.Failure("Brak akcji do cofnięcia")
        val result = command.undo()

        return when(result) {
            is Result.Failure -> result
            is Result.Success<T> -> {
                redoStack.addLast(command)
                result
            }
        }
    }

    fun redo(): Result<T> {
        val command = redoStack.removeLastOrNull() ?: return Result.Failure("Brak akcji do ponowienia")
        val result = command.execute()
        if (result is Result.Success) {
            undoStack.addLast(command)
        }
        return result
    }

    fun canUndo() = undoStack.isNotEmpty()
    fun canRedo() = redoStack.isNotEmpty()
}