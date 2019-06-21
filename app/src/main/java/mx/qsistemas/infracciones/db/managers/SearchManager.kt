package mx.qsistemas.infracciones.db.managers

import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.IdentifierDocument
import mx.qsistemas.infracciones.modules.search.adapters.ResumeInfraItem

object SearchManager {
    fun getFilterDocIdent():MutableList<IdentifierDocument> {
        return object: AsyncTask<Void, Void, MutableList<IdentifierDocument>>(){
            override fun doInBackground(vararg p0: Void?): MutableList<IdentifierDocument>? {
                return Application.m_database?.identifierDocumentDao()?.getAll()
            }

        }.execute().get()


    }
    fun getFilterData(type: Int, filter: String ):MutableList<ResumeInfraItem>{
        return object: AsyncTask<Void, Void, MutableList<ResumeInfraItem>>(){
            override fun doInBackground(vararg p0: Void?): MutableList<ResumeInfraItem> {
               return Application.m_database?.
            }

        }
    }


}