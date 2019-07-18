package mx.qsistemas.infracciones.db.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.Config

@SuppressLint("StaticFieldLeak")
object PrintTicketManager {

    fun getConfig(): Config {
        return object : AsyncTask<Void, Void, Config>() {
            override fun doInBackground(vararg p0: Void?): Config {
                return Application.m_database?.configDao()?.selectFirstConfig()
                        ?: Config(0, 0F, 0, 0F, "", "", "", "", "", "", "", "", "", "", "", "", "",
                                "", 0, 0, 0)
            }
        }.execute().get()
    }
}