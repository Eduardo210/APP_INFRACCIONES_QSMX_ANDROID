package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.*

@SuppressLint("StaticFieldLeak")
object SendInfractionManagerWeb {
    fun getInfractionsToSend(): MutableList<InfringementInfringements> = runBlocking {
        return@runBlocking Application.m_database_web.infractionDaoWeb().selectInfractionsToSend()
    }

    fun getInfractionPictures(idInfraction: Long): MutableList<InfringementPicturesInfringement> = runBlocking {
        return@runBlocking Application.m_database_web.pictureInfractionDaoWeb().selectPicturesToSend(idInfraction)
    }

    fun getInfractionAddress(idInfraction: Long): InfringementAddressInfringement = runBlocking {
        return@runBlocking Application.m_database_web.addressInfringementDaoWeb().selectInfractionAddres(idInfraction)
    }

    fun getInfractionMotivationList(idInfraction: Long): MutableList<InfringementRelfractionInfringements> = runBlocking {
        return@runBlocking Application.m_database_web.infractionFractionDaoWeb().selectFractions(idInfraction)
    }

    fun getPersonLicense(idInfraction: Long): DriverDriverLicense = runBlocking {
        return@runBlocking Application.m_database_web.driverLicenseDaoWeb().selectDriverLicense(idInfraction)
    }

    fun getPersonAddress(idDriver: Long): DriverAddressDriver = runBlocking {
        return@runBlocking Application.m_database_web.addressPersonDaoWeb().selectPersonAddress(idDriver)
    }

    fun getPersonInformation(idInfraction: Long): DriverDrivers = runBlocking {
        return@runBlocking Application.m_database_web.personDaoWeb().selectPersonInfo(idInfraction)
    }

    fun getPayerInformation(idInfraction: Long): ElectronicBill = runBlocking {
        return@runBlocking Application.m_database_web.electronicBillDaoWeb().selectElectronicBill(idInfraction)
    }

    fun getVehicleInformation(idInfraction: Long): VehicleVehicles = runBlocking {
        return@runBlocking Application.m_database_web.vehicleInfractionDaoWeb().selectVehicle(idInfraction)
    }

    fun getCaptureLines(idInfraction: Long): MutableList<InfringementCapturelines> = runBlocking {
        return@runBlocking Application.m_database_web.captureLineDaoWeb().selectCaptureLine(idInfraction)
    }

    fun getPayments(): MutableList<InfringementPayorderToSend> = runBlocking {
        return@runBlocking Application.m_database_web.payorderDaoWeb().selectToSend()
    }

    fun updateInfractionSend(tokenServer: String, folio: String) {
        GlobalScope.launch {
            Application.m_database_web.infractionDaoWeb().updateSendByFolio(tokenServer, folio)
        }
    }

    fun updatePaymentSend(idPayment: Long) {
        GlobalScope.launch {
            Application.m_database_web.payorderDaoWeb().update(idPayment)
        }
    }
}