package com.an.facefilters.canvas.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Failure(val message: String): Result<Nothing>()
}