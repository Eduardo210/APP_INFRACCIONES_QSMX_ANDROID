package mx.qsistemas.infracciones.db.managers

import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.InfractionItem
import mx.qsistemas.infracciones.db.entities.InfractionLocal
import mx.qsistemas.infracciones.singletons.SingletonTicket

object SearchManager {

    fun getItemInfraction(): MutableList<InfractionItem> {
        return object : AsyncTask<Void, Void, MutableList<InfractionItem>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionItem> {
                return Application.m_database?.infractionDao()?.searchResumeInfraction()!!
            }
        }.execute().get()
    }

    fun getInfractionById(idInfraction: Long): InfractionLocal {
        return object : AsyncTask<Void, Void, InfractionLocal>() {
            override fun doInBackground(vararg p0: Void?): InfractionLocal {
                return Application.m_database?.infractionDao()?.getAllDataInraction(idInfraction)!!
            }
        }.execute().get()
    }
    fun getInfraFracc(idInfraction: Long): MutableList<SingletonTicket.ArticleFraction> {
        return object : AsyncTask<Void, Void, MutableList<SingletonTicket.ArticleFraction>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<SingletonTicket.ArticleFraction> {
                return Application.m_database?.infractionDao()?.getInfractionFraction(idInfraction)!!
            }
        }.execute().get()
    }
}
