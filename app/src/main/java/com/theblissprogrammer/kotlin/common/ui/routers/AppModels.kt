package com.theblissprogrammer.kotlin.common.ui.routers

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
sealed class AppModels {
    class Error(
            val title: String,
            val message: String): AppModels()
}