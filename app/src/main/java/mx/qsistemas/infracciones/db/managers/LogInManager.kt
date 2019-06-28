package mx.qsistemas.infracciones.db.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.dao.PersonAccountDao

@SuppressLint("StaticFieldLeak")
object LogInManager {

    fun getUser(userName: String): PersonAccountDao.LogInUser? {
        return object : AsyncTask<Void, Void, PersonAccountDao.LogInUser>() {
            override fun doInBackground(vararg p0: Void?): PersonAccountDao.LogInUser? {
                return Application.m_database?.personAccountDao()?.selectUserByName(userName)
            }
        }.execute().get()
    }
}