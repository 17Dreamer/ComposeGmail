package com.dreamtech.compose.gmail.core.domain

sealed class Result(
    val message: String? = null
) {

    class Success() : Result()

    class Error(message: String? = null) : Result(message)

}