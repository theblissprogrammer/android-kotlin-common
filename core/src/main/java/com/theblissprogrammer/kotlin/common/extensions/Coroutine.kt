package com.theblissprogrammer.kotlin.common.extensions

import com.theblissprogrammer.kotlin.common.common.CompletionResponse
import com.theblissprogrammer.kotlin.common.common.LiveResult
import com.theblissprogrammer.kotlin.common.common.Result
import com.theblissprogrammer.kotlin.common.common.Result.Companion.failure
import com.theblissprogrammer.kotlin.common.errors.DataError
import kotlinx.coroutines.*
import java.io.IOException

/**
 * Created by ahmedsaad on 2019-01-14.
 */
fun <T> coroutineCompletionOnUi (completion: CompletionResponse<T>? = null, call: suspend () -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        try {
            call()
        } catch (e: IOException){
            completion?.invoke(failure(
                    DataError.NetworkFailure(e)
            ))
        }
    }
}

fun <T> coroutineNetwork (call: suspend () -> Result<T>): Deferred<Result<T>> {
    return GlobalScope.async(Dispatchers.IO) {
        call()
    }
}

fun <T> coroutineRoom (call: () -> LiveResult<T>): Deferred<LiveResult<T>> {
    return GlobalScope.async(Dispatchers.IO) {
        call()
    }
}

fun coroutineOnUi (call: suspend () -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        call()
    }
}

fun <T> suspendCoroutineOnUi (call: () -> T, completion: (T) -> Unit) {
    coroutineOnUi {
        completion(suspendCoroutineOnNetwork(call))
    }
}

private suspend fun <T> suspendCoroutineOnNetwork(call: () -> T): T {
    return GlobalScope.async(Dispatchers.IO) {
        call()
    }.await()
}

fun coroutine (call: suspend () -> Unit): Deferred<Unit> {
    return GlobalScope.async {
        call()
    }
}