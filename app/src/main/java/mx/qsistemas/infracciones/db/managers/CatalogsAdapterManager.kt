package mx.qsistemas.infracciones.db.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.net.catalogs.InfractionList

@SuppressLint("StaticFieldLeak")
object CatalogsAdapterManager {

    fun getBrandList(): MutableList<VehicleBrand> {
        return object : AsyncTask<Void, Void, MutableList<VehicleBrand>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<VehicleBrand> {
                return Application.m_database?.vehicleBrandDao()?.selectAll()!!
            }
        }.execute().get()
    }

    fun getSubBrandList(idBrand: Int): MutableList<SubmarkingVehicle> {
        return object : AsyncTask<Void, Void, MutableList<SubmarkingVehicle>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<SubmarkingVehicle> {
                return Application.m_database?.submarkingVehicleDao()?.selectSubBrandsByParent(idBrand)!!
            }
        }.execute().get()
    }

    fun getTypeVehicleList(): MutableList<VehicleType> {
        return object : AsyncTask<Void, Void, MutableList<VehicleType>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<VehicleType> {
                return Application.m_database?.vehicleTypeDao()?.selectAll()!!
            }
        }.execute().get()
    }

    fun getColorList(): MutableList<Colour> {
        return object : AsyncTask<Void, Void, MutableList<Colour>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<Colour> {
                return Application.m_database?.colourDao()?.selectAll()!!
            }
        }.execute().get()
    }

    fun getIdentifierDocList(): MutableList<IdentifierDocument> {
        return object : AsyncTask<Void, Void, MutableList<IdentifierDocument>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<IdentifierDocument> {
                return Application.m_database?.identifierDocumentDao()?.selectAll()!!
            }
        }.execute().get()
    }

    fun getAuthorityIssueList(): MutableList<AuthorityIssues> {
        return object : AsyncTask<Void, Void, MutableList<AuthorityIssues>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<AuthorityIssues> {
                return Application.m_database?.authorityIssuesDao()?.selectAll()!!
            }
        }.execute().get()
    }
    /*fun getInfractionsList(): MutableList<InfractionList.Results>{
        return object : AsyncTask<Void, Void, MutableList<InfractionList.Results>>(){
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionList.Results> {

            }

        }
    }*/



}