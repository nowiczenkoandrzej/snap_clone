package com.an.core_editor.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Failure(val message: String): Result<Nothing>()
}

inline fun <T> Result<T>.handle(
    onSuccess: (T) -> Unit,
    onFailure: (String) -> Unit
) {
    when(this) {
        is Result.Failure -> onFailure(message)
        is Result.Success -> onSuccess(data)
    }
}