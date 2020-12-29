package mx.qsistemas.infracciones.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preferences(val context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveData(key: Int, data: String) {
        val editor = this.preferences.edit()
        editor.putString(context.getString(key), data)
        editor.apply()
    }

    fun saveDataBool(key: Int, data: Boolean) {
        val editor = this.preferences.edit()
        editor.putBoolean(context.getString(key), data)
        editor.apply()
    }

    fun containsData(key: Int): Boolean {
        return this.preferences.getBoolean(context.getString(key), false)
    }

    fun loadDataBoolean(key: Int, defValue: Boolean): Boolean {
        return this.preferences.getBoolean(context.getString(key), defValue)
    }

    fun loadData(key: Int, def: String): String? {
        return this.preferences.getString(context.getString(key), def)
    }

    fun getDataString(key: Int): String? {
        return this.preferences.getString(context.getString(key), "")
    }

    fun clearPreferences() {
        val editor = this.preferences.edit()
        editor.clear()
        editor.apply()
        return
    }

    fun clearPreference(key: Int) {
        val editor = this.preferences.edit()
        editor.remove(context.getString(key))
        editor.apply()
        return
    }

    fun saveData(key: Int, data: Long) {
        val editor = this.preferences.edit()
        editor.putLong(context.getString(key), data)
        editor.apply()
    }

    fun loadData(key: Int): Long {
        return this.preferences.getLong(context.getString(key), -1)
    }
}