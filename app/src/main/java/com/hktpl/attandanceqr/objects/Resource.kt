package com.hktpl.attandanceqr.objects;

sealed class Resource<T>(val data: T?= null,val message: String? = null) {
    class Loading<T>(): Resource<T>()
    class Error<T>(msg: String?): Resource<T>(message = msg)
    class Success<T>(data: T?): Resource<T>(data = data)
}
