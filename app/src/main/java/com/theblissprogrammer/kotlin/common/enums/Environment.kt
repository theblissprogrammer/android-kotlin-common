package com.theblissprogrammer.kotlin.common.enums

import com.theblissprogrammer.kotlin.common.BuildConfig


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

enum class Environment {
    DEVELOPMENT,
    STAGING,
    PRODUCTION;
    companion object {
        var mode: Environment = {
            when {
                BuildConfig.FLAVOR == "dev" -> DEVELOPMENT
                BuildConfig.FLAVOR == "uat" -> STAGING
                else -> PRODUCTION
            }
        }()
    }
}