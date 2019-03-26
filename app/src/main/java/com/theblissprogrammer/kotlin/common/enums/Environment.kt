package com.theblissprogrammer.kotlin.common.enums

import com.theblissprogrammer.kotlin.common.BuildConfig


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

enum class Environment {
    DEVELOPMENT,
    PRODUCTION;
    companion object {
        var mode: Environment = {
            if (BuildConfig.FLAVOR.equals("dev"))
                DEVELOPMENT
            else
                PRODUCTION
        }()
    }
}