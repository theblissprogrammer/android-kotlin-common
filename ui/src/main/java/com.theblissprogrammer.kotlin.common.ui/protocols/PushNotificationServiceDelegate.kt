package com.theblissprogrammer.kotlin.common.ui.protocols

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

interface PushNotificationServiceDelegate {
    fun onTokenRefresh(deviceToken: String)
}