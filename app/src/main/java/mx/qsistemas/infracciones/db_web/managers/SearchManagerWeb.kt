package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.runBlocking
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.InfractionItem


@SuppressLint("StaticFieldLeak")
object SearchManagerWeb {
    fun getItemInfraction(query: SupportSQLiteQuery): MutableList<InfractionItem> = runBlocking {
         object : AsyncTask<Void, Void, MutableList<InfractionItem>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionItem> {
                return Application.m_database_web?.infractionDaoWeb()?.searchResumeInfraction(query)!!
            }
        }.execute().get()
    }
}