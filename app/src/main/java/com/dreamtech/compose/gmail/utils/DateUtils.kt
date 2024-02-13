package com.dreamtech.compose.gmail.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


fun Long.format(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val pattern = if (calendar.isToday()) {
        "h:mm a"
    } else {
        "MMM dd"
    }

    val formatter = SimpleDateFormat(pattern, Locale.US)
    return formatter.format(calendar.time)
}

fun Long.metadataFormat(): String {
    val bestDateFormat =
        android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEMMMMddyyyy")
    val formatter = SimpleDateFormat(bestDateFormat, Locale.US)
    return formatter.format(this)
}

fun Calendar.isToday(): Boolean {
    val calendar = Calendar.getInstance()
    return get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && get(Calendar.MONTH) == calendar.get(
        Calendar.MONTH
    ) && get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
}