package com.rudyrachman16.githubuserapi.data

interface GetResult<T> {
    fun onSuccess(result: T)
}