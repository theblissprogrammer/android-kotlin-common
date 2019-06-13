package com.theblissprogrammer.kotlin.common.dependencies

import android.app.Application
import android.content.Context
import com.theblissprogrammer.kotlin.common.account.AuthenticationNetworkService
import com.theblissprogrammer.kotlin.common.account.AuthenticationService
import com.theblissprogrammer.kotlin.common.account.AuthenticationWorker
import com.theblissprogrammer.kotlin.common.account.AuthenticationWorkerType
import com.theblissprogrammer.kotlin.common.network.APISession
import com.theblissprogrammer.kotlin.common.network.APISessionType
import com.theblissprogrammer.kotlin.common.network.HTTPService
import com.theblissprogrammer.kotlin.common.network.HTTPServiceType
import com.theblissprogrammer.kotlin.common.preferences.*
import com.theblissprogrammer.kotlin.common.security.SecurityPreferenceStore
import com.theblissprogrammer.kotlin.common.security.SecurityStore
import com.theblissprogrammer.kotlin.common.security.SecurityWorker
import com.theblissprogrammer.kotlin.common.security.SecurityWorkerType

open class AppDependency: AppDependable {
    override lateinit var application: Application

    override val resolveContext: Context by lazy {
        application.applicationContext
    }

    override val resolveConstants: ConstantsType by lazy {
        Constants(
            store = resolveConstantsStore
        )
    }

    // Workers

    override val resolvePreferencesWorker: PreferencesWorkerType by lazy {
        PreferencesWorker(store = resolvePreferencesStore)
    }

    override val resolveSecurityWorker: SecurityWorkerType by lazy {
        SecurityWorker(
            context = resolveContext,
            store = resolveSecurityStore,
            constants = resolveConstants
        )
    }

    override val resolveAuthenticationWorker: AuthenticationWorkerType  by lazy {
        AuthenticationWorker(
            service = resolveAuthenticationService,
            preferencesWorker = resolvePreferencesWorker,
            securityWorker = resolveSecurityWorker,
            context = resolveContext
        )
    }


    // Stores

    override val resolveConstantsStore: ConstantsStore by lazy {
        ConstantsResourceStore(
            context = resolveContext
        )
    }

    override val resolvePreferencesStore: PreferencesStore by lazy {
        PreferencesDefaultsStore(context = resolveContext)
    }

    override val resolveSecurityStore: SecurityStore by lazy {
        SecurityPreferenceStore(context = resolveContext)
    }

    // Services

    override val resolveHTTPService: HTTPServiceType by lazy {
        HTTPService()
    }

    override val resolveAPISessionService: APISessionType by lazy {
        APISession(
            context = resolveContext,
            constants = resolveConstants,
            securityWorker = resolveSecurityWorker
        )
    }

    override val resolveAuthenticationService: AuthenticationService  by lazy {
        AuthenticationNetworkService(
            apiSession = resolveAPISessionService
        )
    }
}