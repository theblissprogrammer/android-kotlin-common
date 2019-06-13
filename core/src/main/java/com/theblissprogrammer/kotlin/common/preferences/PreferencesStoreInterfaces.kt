package com.theblissprogrammer.kotlin.common.preferences

import com.theblissprogrammer.kotlin.common.enums.DefaultsKey

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */

interface PreferencesStore {
    fun <T> get(key: DefaultsKey<T?>): T?
    fun <T> set(value: T?, key: DefaultsKey<T?>)
    fun <T> remove(key: DefaultsKey<T?>)
    fun clear()
}

abstract class PreferencesWorkerType: PreferencesStore