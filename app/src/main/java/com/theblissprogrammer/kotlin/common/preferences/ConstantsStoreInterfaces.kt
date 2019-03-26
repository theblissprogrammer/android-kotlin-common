package com.theblissprogrammer.kotlin.common.preferences

import com.theblissprogrammer.kotlin.common.R
import com.theblissprogrammer.kotlin.common.enums.Environment

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

interface ConstantsStore {
    fun <T> get(key: Int, default: T): T
}

interface ConstantsType: ConstantsStore {
    val jwtSecretKey: String

    /*val baseURL: String
        get() = when (Environment.mode) {
            Environment.DEVELOPMENT -> get(R.string.base_url_debug, "" )
            Environment.PRODUCTION -> get(R.string.base_url, "" )
        }

    val baseRESTPath: String
        get() = get(R.string.base_rest_path, "" )*/
}