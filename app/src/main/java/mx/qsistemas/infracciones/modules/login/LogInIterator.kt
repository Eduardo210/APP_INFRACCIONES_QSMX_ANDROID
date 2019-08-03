package mx.qsistemas.infracciones.modules.login

import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.db.managers.CatalogsSyncManager
import mx.qsistemas.infracciones.db.managers.LogInManager
import mx.qsistemas.infracciones.net.catalogs.DownloadCatalogs
import mx.qsistemas.infracciones.utils.MD5

class LogInIterator(private val listener: LogInContracts.Presenter) : LogInContracts.Iterator {

    override fun registerAlarm() {
        Alarms()
    }

    override fun downloadCatalogs() {
       /* NetworkApi().getNetworkService().downloadCatalogs("01/01/2000").enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), DownloadCatalogs::class.java)
                    processCatalogs(data)
                    val imei = Utils.getImeiDevice(Application.getContext())
                    Application.firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.update("last_synch", Date())
                    listener.onCatalogsDownloaded()
                } else {
                    listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t.message ?: "")
            }
        })*/
    }

    override fun login(userName: String, psd: String) {
        val user = LogInManager.getUser(userName)
        val hash = MD5.toMD5(psd)
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

        listener.onLoginSuccessful()
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