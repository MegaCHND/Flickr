package com.example.flickr.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

val originalFormat = SimpleDateFormat(
    "yyyy-MM-dd'T'HH:mm:ss'Z'",
    Locale.US
).apply {
    timeZone = TimeZone.getTimeZone("GMT")
}

val targetFormat = SimpleDateFormat(
    "MMM dd, yyyy 'at' HH:mm",
    Locale.US
)

/**
 * Converts a string in "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" format
 * to a string in "MMM dd, yyyy 'at' HH:mm" format
 **/
fun String.convertDate(): String? = try {
    originalFormat.parse(this)?.let { targetFormat.format(it) }
} catch (e: ParseException) {
    null
}


/**
 * used on a null string to print "" instead of "null"
 * */
fun String?.nullToEmpty(): String = this ?: ""