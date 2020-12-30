package mx.qsistemas.infracciones.db.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.*

@SuppressLint("StaticFieldLeak")
object CatalogsAdapterManager {

    fun getBrandList(): MutableList<VehicleBrand> {
        return object : AsyncTask<Void, Void, MutableList<VehicleBrand>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<VehicleBrand> {
                return Application.m_database.vehicleBrandDao().selectAll()
            }
        }.execute().get()
    }

    fun getSubBrandList(idBrand: Int): MutableList<SubmarkingVehicle> {
        return object : AsyncTask<Void, Void, MutableList<SubmarkingVehicle>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<SubmarkingVehicle> {
                return Application.m_database.submarkingVehicleDao().selectSubBrandsByParent(idBrand)
            }
        }.execute().get()
    }

    fun getTypeVehicleList(): MutableList<VehicleType> {
        return object : AsyncTask<Void, Void, MutableList<VehicleType>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<VehicleType> {
                return Application.m_database.vehicleTypeDao().selectAll()
            }
        }.execute().get()
    }

    fun getColorList(): MutableList<Colour> {
        return object : AsyncTask<Void, Void, MutableList<Colour>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<Colour> {
                return Application.m_database.colourDao().selectAll()
            }
        }.execute().get()
    }

    fun getIdentifierDocList(): MutableList<IdentifierDocument> {
        return object : AsyncTask<Void, Void, MutableList<IdentifierDocument>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<IdentifierDocument> {
                return Application.m_database.identifierDocumentDao().selectAll()
            }
        }.execute().get()
    }

    fun getAuthorityIssueList(): MutableList<AuthorityIssues> {
        return object : AsyncTask<Void, Void, MutableList<AuthorityIssues>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<AuthorityIssues> {
                return Application.m_database.authorityIssuesDao().selectAll()
            }
        }.execute().get()
    }

    fun getArticlesList(): MutableList<Articles> {
        return object : AsyncTask<Void, Void, MutableList<Articles>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<Articles> {
                return Application.m_database.articlesDao().selectAll()
            }
        }.execute().get()
    }

    fun getFractionsList(idArticle: Int): MutableList<InfractionFraction> {
        return object : AsyncTask<Void, Void, MutableList<InfractionFraction>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionFraction> {
                return Application.m_database.infractionFractionDao().selectByArticle(idArticle)
            }
        }.execute().get()
    }

    fun getRetainedDocList(): MutableList<RetainedDocument> {
        return object : AsyncTask<Void, Void, MutableList<RetainedDocument>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<RetainedDocument> {
                return Application.m_database.retainedDocumentDao().selectAll()
            }
        }.execute().get()
    }

    fun getDispositionList(): MutableList<Disposition> {
        return object : AsyncTask<Void, Void, MutableList<Disposition>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<Disposition> {
                return Application.m_database.dispositionDao().selectAll()
            }
        }.execute().get()
    }

    fun getLicenseTypeList(): MutableList<LicenseType> {
        return object : AsyncTask<Void, Void, MutableList<LicenseType>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<LicenseType> {
                return Application.m_database.licenseTypeDao().selectAll()
            }
        }.execute().get()
    }
}