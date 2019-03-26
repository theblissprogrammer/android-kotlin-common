package com.theblissprogrammer.kotlin.common.ui.extentions

import android.app.Activity
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.theblissprogrammer.kotlin.common.R
import com.theblissprogrammer.kotlin.common.ui.CoreApplication
import com.theblissprogrammer.kotlin.common.ui.controls.SpinnerDialogFragment


fun AppCompatActivity.replaceFragment(fragment: Fragment) {

    supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, fragment)
            .commitAllowingStateLoss()
}

fun Fragment.replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    val transaction = fragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_enter, R.anim.right_exit)
            ?.replace(R.id.fragment_holder, fragment,
                    fragment::class.java.simpleName)

    if (addToBackStack)
        transaction?.addToBackStack(fragment::class.java.simpleName)

    transaction?.commitAllowingStateLoss()
}

fun AppCompatActivity.fullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun getString(resId: Int, vararg formatArgs: Any) = CoreApplication.instance.getString(resId, *formatArgs)


fun Fragment.createSpinner(message: String = getString(R.string.loading))
        = SpinnerDialogFragment.newInstance(message, activity = activity as? AppCompatActivity)

fun SpinnerDialogFragment.showSpinner(tag: String = "spinner") {
    if(!isAdded || !isVisible)
        activity?.supportFragmentManager?.let {
            show(it, tag)
        }
}

fun SpinnerDialogFragment.hideSpinner() = dismissAllowingStateLoss()

fun Activity.closeKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}