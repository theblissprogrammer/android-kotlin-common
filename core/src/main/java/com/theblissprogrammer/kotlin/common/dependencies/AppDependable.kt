package com.theblissprogrammer.kotlin.common.dependencies

import android.app.Application
import android.content.Context
import com.theblissprogrammer.kotlin.common.account.AuthenticationService
import com.theblissprogrammer.kotlin.common.account.AuthenticationWorkerType
import com.theblissprogrammer.kotlin.common.network.APISessionType
import com.theblissprogrammer.kotlin.common.network.HTTPServiceType
import com.theblissprogrammer.kotlin.common.preferences.ConstantsStore
import com.theblissprogrammer.kotlin.common.preferences.ConstantsType
import com.theblissprogrammer.kotlin.common.preferences.PreferencesStore
import com.theblissprogrammer.kotlin.common.preferences.PreferencesWorkerType
import com.theblissprogrammer.kotlin.common.security.SecurityStore
import com.theblissprogrammer.kotlin.common.security.SecurityWorkerType


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
interface AppDependable {
    var application: Application

    val resolveContext: Context

    val resolveConstants: ConstantsType

    val resolvePreferencesWorker: PreferencesWorkerType
    val resolveSecurityWorker: SecurityWorkerType
    val resolveAuthenticationWorker: AuthenticationWorkerType

    val resolveConstantsStore: ConstantsStore
    val resolvePreferencesStore: PreferencesStore
    val resolveSecurityStore: SecurityStore

    val resolveHTTPService: HTTPServiceType
    val resolveAPISessionService: APISessionType

    val resolveAuthenticationService: AuthenticationService
}