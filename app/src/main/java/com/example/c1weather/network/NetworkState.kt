package com.example.c1weather.network


sealed class NetworkState<out T> {
    data class Success<T> (val result: T) : NetworkState<T>()
    data class Error(val message: String) : NetworkState<Nothing>()
    object Loading: NetworkState<Nothing>()
}