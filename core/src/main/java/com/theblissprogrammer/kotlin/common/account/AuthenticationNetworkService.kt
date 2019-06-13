package com.theblissprogrammer.kotlin.common.account

import com.theblissprogrammer.kotlin.common.network.APISessionType

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
class AuthenticationNetworkService(val apiSession: APISessionType) : AuthenticationService {

    /// Determines if the user is signed in with a token for authorized requests.
    override val isAuthorized
            get() = apiSession.isAuthorized
}