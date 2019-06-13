package com.theblissprogrammer.kotlin.common.ui.protocols


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
interface AuthenticationServiceDelegate {
    fun authenticationDidLogin(userID: String)
    fun authenticationDidLogout(userID: String)
}