package com.example.therapy_flow.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val prefs: SharedPreferences) {

    var currentUserId: String?
        get() = prefs.getString(USER_ID, null)
        set(value) = prefs.edit().putString(USER_ID, value).apply()

    var authToken: String?
        get() = prefs.getString(TOKEN, null)
        set(value) = prefs.edit().putString(TOKEN, value).apply()
}
