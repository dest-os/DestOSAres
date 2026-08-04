package com.destos.ares.utils

import android.content.Context

object PrefsManager {
    private const val PREFS = "dest_os_prefs"
    private const val KEY_LANG = "language"
    private const val KEY_GENDER = "gender"

    fun saveSelections(context: Context, language: String, gender: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANG, language)
            .putString(KEY_GENDER, gender)
            .apply()
    }

    fun getLanguage(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANG, "tr") ?: "tr"

    fun getGender(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_GENDER, "female") ?: "female"
}
