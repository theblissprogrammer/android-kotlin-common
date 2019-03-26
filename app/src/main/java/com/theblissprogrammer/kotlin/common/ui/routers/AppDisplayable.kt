package com.theblissprogrammer.kotlin.common.ui.routers

import com.theblissprogrammer.kotlin.common.ui.BaseFragment
import com.theblissprogrammer.kotlin.common.ui.routers.AppModels

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
interface AppDisplayable {
    fun display(error: AppModels.Error) {
        when (this) {
            is BaseFragment -> {
                this.present(title = error.title, message = error.message)
            }
            else -> {}
        }
    }

    fun displaySupport(error: AppModels.Error) {
        display(error)
    }
}