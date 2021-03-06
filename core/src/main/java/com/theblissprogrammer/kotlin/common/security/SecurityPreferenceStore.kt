package com.theblissprogrammer.kotlin.common.security

import android.content.Context
import android.content.SharedPreferences
import com.theblissprogrammer.kotlin.common.enums.SecurityProperty
import com.theblissprogrammer.kotlin.common.enums.SecurityProperty.*

/**
* Created by theblissprogrammer on 10/7/15.
* Copyright © 2019. All rights reserved.
*/
class SecurityPreferenceStore(val context: Context?): SecurityStore {
    val preferences: SharedPreferences? = context?.
            getSharedPreferences("app_user_security", Context.MODE_PRIVATE)

    override fun get(key: SecurityProperty) = preferences?.getString(key.id, null)

    override fun set(key: SecurityProperty, value: String?): Boolean {
        return if (value != null) {
            val editor = preferences?.edit()
            editor?.putString(key.id, value)
            editor?.commit() ?: false
        } else {
            delete(key)
        }
    }

    override fun delete(key: SecurityProperty): Boolean {
        val editor = preferences?.edit()
        editor?.remove(key.id)
        return editor?.commit() ?: false
    }

    /// Removes all the keystore items.
    override fun clear() {
        delete(JWT)
        delete(TOKEN)
        delete(EMAIL)
        delete(PASSWORD)
    }
}