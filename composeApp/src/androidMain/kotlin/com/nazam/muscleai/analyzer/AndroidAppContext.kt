package com.nazam.muscleai.analyzer

import android.content.Context

/**
 * Simple provider de Context pour MediaPipe.
 * Init dans MainActivity.
 */
object AndroidAppContext {
    private var appContext: Context? = null

    fun init(context: Context) {
        if (appContext == null) appContext = context.applicationContext
    }

    fun get(): Context = requireNotNull(appContext) {
        "AndroidAppContext not initialized. Call AndroidAppContext.init(...) in MainActivity."
    }
}
