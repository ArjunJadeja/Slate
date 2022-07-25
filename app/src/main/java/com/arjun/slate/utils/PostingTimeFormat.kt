package com.arjun.slate.utils

import java.text.SimpleDateFormat

class PostingTimeFormat {

    companion object {

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS

        private val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")

        fun getPostTime(time: Long): String? {

            val now: Long = System.currentTimeMillis()
            if (time > now || time <= 0) {
                return null
            }
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff / MINUTE_MILLIS < 2) {
                "a minute ago"
            } else if (diff / MINUTE_MILLIS < 60) {
                (diff / MINUTE_MILLIS).toString() + " minutes ago"
            } else if (diff / HOUR_MILLIS < 2) {
                "an hour ago"
            } else if (diff / HOUR_MILLIS <= 24) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else {
                simpleDateFormat.format(time)
            }
        }
    }
}