package me.yamlee.jsbridge.utils

import android.util.Log
import me.yamlee.jsbridge.AndroidJsBridge

object LogUtil {
    private const val TAG = "AndroidJsBridge"

    fun info(message: String) {
        if (AndroidJsBridge.mDebug) {
            Log.i(TAG, message)
        }
    }

    fun error(message: String) {
        if (AndroidJsBridge.mDebug) {
            Log.e(TAG, message)
        }
    }

    fun error(e: Throwable) {
        if (AndroidJsBridge.mDebug) {
            e.printStackTrace()
            Log.e(TAG, e.message)
        }
    }
}