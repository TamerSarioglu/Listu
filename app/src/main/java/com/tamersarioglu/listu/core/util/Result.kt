package com.tamersarioglu.listu.core.util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun exceptionOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }

    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) action(exception)
        return this
    }

    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) action()
        return this
    }

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(exception: Throwable): Result<Nothing> = Error(exception)
        fun loading(): Result<Nothing> = Loading
    }
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.success(transform(data))
    is Result.Error -> this
    is Result.Loading -> this
}

suspend inline fun <T> safeCall(action: suspend () -> T): Result<T> = try {
    Result.success(action())
} catch (e: Exception) {
    Result.error(e)
}