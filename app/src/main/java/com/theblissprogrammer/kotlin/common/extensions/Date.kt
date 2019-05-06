package com.theblissprogrammer.kotlin.common.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ahmedsaad on 2018-01-05.
 * Copyright © 2017. All rights reserved.
 */

fun Date.shortString(): String {
    val outputFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return outputFmt.format(this)
}

fun Date.longString(): String {
    val outputFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
    return outputFmt.format(this)
}

fun Date.add(field: Int = Calendar.DATE, value: Int = 0): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(field, value)
    return cal.time
}

val Date.isDateInWeekend: Boolean
    get() {
        val cal = Calendar.getInstance()
        cal.time = this

        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

val Date.isDateInToday: Boolean
    get() {
        return this.startOfDay() == Date().startOfDay()
    }

fun Date.startOfDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.time
}

fun Date.readableDifference(date: Date?): String {
    if (date == null) return ""

    val difference = Math.abs(this.time - date.time)

    val res = when {
        difference < 1000 * 60 -> {
            // Seconds
            String.format("%d seconds", difference / 1000)
        }
        difference < 1000 * 60 * 60 -> {
            // Minutes
            String.format("%d minutes", difference / (1000 * 60))
        }
        difference < 1000 * 60 * 60 * 24 -> {
            // Hours
            String.format("%d hours", difference / (1000 * 60 * 60))
        }
        difference < 1000 * 60 * 60 * 24 * 7 -> {
            // Days
            String.format("%d days", difference / (1000 * 60 * 60 * 24))
        }
        else -> {
            // Weeks
            String.format("%d weeks", difference / (1000 * 60 * 60 * 24 * 7))
        }
    }

    return res + if (this.before(date)) " from now" else " ago"
}