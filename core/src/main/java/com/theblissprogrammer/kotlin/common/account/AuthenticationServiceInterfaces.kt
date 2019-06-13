package com.theblissprogrammer.kotlin.common.account

import com.theblissprogrammer.kotlin.common.account.models.AccountModels
import com.theblissprogrammer.kotlin.common.account.models.LoginModels
import com.theblissprogrammer.kotlin.common.common.CompletionResponse


/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

interface AuthenticationService {
    val isAuthorized: Boolean
}

interface AuthenticationWorkerType {
    val isAuthorized: Boolean
    suspend fun login(request: LoginModels.Request,
                      completion: CompletionResponse<AccountModels.Response>)
    fun logout()
}