package com.theblissprogrammer.kotlin.common.common

import com.theblissprogrammer.kotlin.common.errors.DataError
import com.theblissprogrammer.kotlin.common.errors.NetworkError


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

class NoInternetException: Exception()

fun initDataError(error: NetworkError?): DataError {
    // Handle no internet
    if (error?.internalError is NoInternetException) {
        return DataError.NoInternet
    }

    // Handle by status code
    return when (error?.statusCode) {
        400 -> DataError.NetworkFailure(error)
        401 -> DataError.Unauthorized
        403 -> DataError.Forbidden
        else -> DataError.ServerFailure(error?.fieldErrors ?: mutableMapOf())
    }
}