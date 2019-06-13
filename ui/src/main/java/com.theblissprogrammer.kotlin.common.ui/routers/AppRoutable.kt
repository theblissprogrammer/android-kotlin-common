package com.theblissprogrammer.kotlin.common.ui.routers

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

/**
 * Created by ahmedsaad on 2019-01-14.
 * Copyright © 2019. All rights reserved.
 */
interface AppRoutable {
    var fragment: WeakReference<Fragment?>

    fun dismiss(isFragment: Boolean = false) {
        if (isFragment) {
            fragment.get()?.fragmentManager?.popBackStack() ?: fragment.get()?.activity?.finish()
        } else {
            fragment.get()?.activity?.finish()
        }
    }
}