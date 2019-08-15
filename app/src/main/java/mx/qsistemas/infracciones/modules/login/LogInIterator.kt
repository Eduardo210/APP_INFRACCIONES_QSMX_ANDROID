package mx.qsistemas.infracciones.modules.login

import android.util.Log
import com.google.firebase.functions.FirebaseFunctionsException
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.db.managers.CatalogsSyncManager
import mx.qsistemas.infracciones.net.catalogs.DownloadCatalogs
import mx.qsistemas.infracciones.utils.BBOX_KEY
import mx.qsistemas.infracciones.utils.FF_CIPHER_DATA
import java.util.*

class LogInIterator(private val listener: LogInContracts.Presenter) : LogInContracts.Iterator {

    override fun registerAlarm() {
        Alarms()
    }

    override fun login(userName: String, psd: String) {
        val request = hashMapOf("key" to BBOX_KEY, "value" to psd)
        Application.firebaseFunctions?.getHttpsCallable(FF_CIPHER_DATA)?.call(request)?.addOnCompleteListener {
            if (!it.isSuccessful) {
                val e = it.exception
                if (e is FirebaseFunctionsException)
                    listener.onError(e.details.toString())
                else
                    listener.onError(Application.getContext().getString(R.string.e_without_internet))
                return@addOnCompleteListener
            }
            if (it.exception == null && it.result != null){
                Log.e(this.javaClass.simpleName, "result ${it.result?.data}")
                val cipher = ((it.result?.data) as HashMap<*, *>)["encrypted"].toString()

            }
        }
        /*if (user == null || hash != user.password) {
            listener.onError(Application.getContext().getString(R.string.e_user_pss_incorrect))
        } else {
            Application.prefs?.saveDataInt(R.string.sp_id_township_person, user.idPersonTownship)
            Application.prefs?.saveDataInt(R.string.sp_id_person, user.idPerson)
            Application.prefs?.saveData(R.string.sp_person_name, user.name)
            Application.prefs?.saveData(R.string.sp_person_f_last_name, user.fLastName)
            Application.prefs?.saveData(R.string.sp_person_m_last_name, user.mLastName)
            Application.prefs?.saveData(R.string.sp_no_employee, user.employee)
            FirebaseEvents.registerUserProperties()
            listener.onLoginSuccessful()
        }*/

        //listener.onLoginSuccessful()
    }

    private fun processCatalogs(data: DownloadCatalogs) {
        CatalogsSyncManager.savePersonAttribute(data.personAttribute)
        CatalogsSyncManager.savePersonAccount(data.personAccount)
        CatalogsSyncManager.saveAdscription(data.adscription)
        CatalogsSyncManager.saveAttribute(data.attribute)
        CatalogsSyncManager.saveColor(data.color)
        CatalogsSyncManager.saveConfiguration(data.configuration)
        CatalogsSyncManager.saveNonWorkingDay(data.nonWorkingDay)
        CatalogsSyncManager.saveState(data.state)
        CatalogsSyncManager.saveArticleInfraction(data.articleInfraction)
        CatalogsSyncManager.saveAuthorityExpedition(data.authorityExpedition)
        CatalogsSyncManager.saveInfractionDisposition(data.infractionDisposition)
        CatalogsSyncManager.saveIdentifierDocument(data.identifierDocument)
        CatalogsSyncManager.saveRetainedDocument(data.retainedDocument)
        CatalogsSyncManager.saveFractionInfraction(data.fractionInfraction)
        CatalogsSyncManager.saveTypeLicense(data.typeLicense)
        CatalogsSyncManager.saveTypeVehicle(data.typeVehicle)
        CatalogsSyncManager.saveBrandVehicle(data.brandVehicle)
        CatalogsSyncManager.saveModule(data.module)
        CatalogsSyncManager.saveSepomex(data.townshipSepomex)
        CatalogsSyncManager.savePerson(data.person)
        CatalogsSyncManager.saveTownhallPerson(data.townhallPerson)
        CatalogsSyncManager.saveSubBrandVehicle(data.subBrandVehicle)
        CatalogsSyncManager.saveSynch(data.synchonization)
    }
}