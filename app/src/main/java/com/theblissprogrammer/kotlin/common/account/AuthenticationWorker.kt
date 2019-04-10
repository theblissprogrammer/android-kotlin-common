package com.theblissprogrammer.kotlin.common.account

import android.content.Context
import android.content.Intent
import com.theblissprogrammer.kotlin.common.account.models.AccountModels
import com.theblissprogrammer.kotlin.common.account.models.LoginModels
import com.theblissprogrammer.kotlin.common.common.CompletionResponse
import com.theblissprogrammer.kotlin.common.common.Result
import com.theblissprogrammer.kotlin.common.common.Result.Companion.failure
import com.theblissprogrammer.kotlin.common.common.Result.Companion.success
import com.theblissprogrammer.kotlin.common.enums.DefaultsKeys.Companion.userID
import com.theblissprogrammer.kotlin.common.enums.SecurityProperty
import com.theblissprogrammer.kotlin.common.errors.DataError
import com.theblissprogrammer.kotlin.common.extensions.ACTION_AUTHENTICATION_DID_LOGIN
import com.theblissprogrammer.kotlin.common.extensions.ACTION_AUTHENTICATION_DID_LOGOUT
import com.theblissprogrammer.kotlin.common.extensions.USER_ID
import com.theblissprogrammer.kotlin.common.logging.LogHelper
import com.theblissprogrammer.kotlin.common.preferences.PreferencesWorkerType
import com.theblissprogrammer.kotlin.common.security.SecurityWorkerType
import androidx.localbroadcastmanager.content.LocalBroadcastManager


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
class AuthenticationWorker(val service: AuthenticationService,
                           val preferencesWorker: PreferencesWorkerType,
                           val securityWorker: SecurityWorkerType,
                           val context: Context?) : AuthenticationWorkerType {

    /// Determine if user is authenticated on server and local level
    override val isAuthorized
            get() = service.isAuthorized && !preferencesWorker.get(userID).isNullOrEmpty()

    override suspend fun login(request: LoginModels.Request,
                               completion: CompletionResponse<AccountModels.Response>) {
        // Validate input
        if (request.username.isBlank() ||
                request.password.isBlank()) {
           return completion(failure(DataError.Incomplete))
        }

        authenticated(request = request) {

            // Notify application authentication occurred and ready
            if (context != null){
                val intent = Intent()
                intent.action = ACTION_AUTHENTICATION_DID_LOGIN
                intent.putExtra(USER_ID, it.value?.userID)

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }

            LogHelper.i(messages = *arrayOf("Login complete for user #${it.value?.userID}."))

            completion(success(it.value))
        }
    }

    override fun logout() {
        // Save logged out user ID before cleared out
        val userID = preferencesWorker.get(userID)

        LogHelper.d(messages = *arrayOf("Log out for user #${userID ?: ""} begins..."))

        securityWorker.clear()
        preferencesWorker.clear()

        LogHelper.i(messages = *arrayOf("Log out complete for user #${userID ?: 0}."))

        // Notify application authentication occurred and ready
        val context = context ?: return

        val intent = Intent()
        intent.action = ACTION_AUTHENTICATION_DID_LOGOUT
        intent.putExtra(USER_ID, userID ?: "")

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    /// Used for handling successfully completed signup or login
    private suspend fun authenticated(request: LoginModels.Request,
                                      completion: suspend (Result<AccountModels.Response>) -> Unit){

        securityWorker.clear()

        // Store user info for later use on app start and db initialization
        // NOTE: In a production app token and id would be retrieved from the api. Here we just hardcode it.
        securityWorker.set(token = "Dummy Token", email = request.username, password = request.password)
        securityWorker.set(key = SecurityProperty.TOKEN, value = "Random Token")
        preferencesWorker.set(request.username, key = userID)

        completion(success(AccountModels.Response(request.username)))
    }
}