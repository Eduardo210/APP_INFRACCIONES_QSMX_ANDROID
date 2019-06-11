package mx.qsistemas.payments_transfer.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import mx.qsistemas.payments_transfer.R

class Preferences(private val context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.pt_sp_name),
            MODE_PRIVATE)

    fun saveData(key: Int, data: String) {
        val editor = this.preferences.edit()
        editor.putString(context.getString(key), data)
        editor.commit()
    }

    fun saveDataBool(key: Int, data: Boolean) {
        val editor = this.preferences.edit()
        editor.putBoolean(context.getString(key), data)
        editor.commit()
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
        editor.commit()
        return
    }

    fun clearPreference(key: Int) {
        val editor = this.preferences.edit()
        editor.remove(context.getString(key))
        editor.commit()
        return
    }

    fun saveDataInt(key: Int, data: Int) {
        val editor = this.preferences.edit()
        editor.putInt(context.getString(key), data)
        editor.commit()
    }

    fun loadDataInt(key: Int): Int {
        return this.preferences.getInt(context.getString(key), -1)
    }
}