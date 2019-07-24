package mx.qsistemas.infracciones.singletons

import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships
import java.util.*

object SingletonInfraction {

    /* Vehicle Information */
    var identifierDocument: GenericCatalog = GenericCatalog("", true)
    var noDocument: String = ""
    var stateIssuedIn: GenericCatalog = GenericCatalog("", true)
    var typeDocument: GenericCatalog = GenericCatalog("", true)
    var brandVehicle: GenericCatalog = GenericCatalog("", true)
    var subBrandVehicle: GenericSubCatalog = GenericSubCatalog("", null,true)
    var colorVehicle: GenericCatalog = GenericCatalog("", true)
    var typeVehicle: GenericCatalog = GenericCatalog("", true)
    var noCirculationCard: String = ""
    var yearVehicle: String = ""
    var evidence1: String = ""
    var evidence2: String = ""

    /* Infracion Information */
    var colonnyInfraction: String = ""
    var streetInfraction: String = ""
    var betweenStreet1: String = ""
    var betweenStreet2: String = ""
    var motivationList: MutableList<DtoMotivation> = mutableListOf()
    var retainedDocument: RetainedDocument = RetainedDocument(0, "")
    var isRemited: Boolean = false
    var dispositionRemited: Disposition = Disposition(0, "")
    var latitudeInfraction: Double = 0.0
    var longitudeInfraction: Double = 0.0
    var dateInfraction: String = ""
    var folioInfraction: String = ""

    /* Offender Information */
    var isPersonAbstent: Boolean = true
    var nameOffender: String = "QUIEN"
    var lastFatherName: String = "RESULTE"
    var lastMotherName: String = "RESPONSABLE"
    var rfcOffenfer: String = ""
    var stateOffender: States = States()
    var townshipOffender: Townships = Townships()
    var colonyOffender: String = ""
    var streetOffender: String = ""
    var noExtOffender: String = ""
    var noIntOffender: String = ""
    var noLicenseOffender: String = ""
    var typeLicenseOffender: LicenseType = LicenseType(0, "")
    var licenseIssuedInOffender: States = States()

    /* Calculated Variables */
    var idNewInfraction: Long = 0L
    var idNewPersonInfraction: Long = 0L
    var idPersonTownship: Long = 0L
    var subTotalInfraction: String = ""
    var discountInfraction: String = ""
    var totalInfraction: String = ""
    var captureLineii: Date? = null
    var captureLineiii: Date? = null
    var amountCaptureLineii: Float = 0F
    var amountCaptureLineiii: Float = 0F
    var paymentAuthCode: String = ""

    fun cleanSingleton() {
        /* Vehicle Information Reset */
        identifierDocument = GenericCatalog("", true)
        noDocument = ""
        stateIssuedIn = GenericCatalog("", true)
        typeDocument = GenericCatalog("", true)
        brandVehicle = GenericCatalog("", true)
        subBrandVehicle = GenericSubCatalog("", null,true)
        colorVehicle = GenericCatalog("", true)
        typeVehicle = GenericCatalog("", true)
        noCirculationCard = ""
        yearVehicle = ""
        evidence1 = ""
        evidence2 = ""

        /* Infraction Information Reset */
        colonnyInfraction = ""
        streetInfraction = ""
        betweenStreet1 = ""
        betweenStreet2 = ""
        motivationList = mutableListOf()
        retainedDocument = RetainedDocument(0, "")
        isRemited = false
        dispositionRemited = Disposition(0, "")
        latitudeInfraction = 0.0
        longitudeInfraction = 0.0
        dateInfraction = ""
        folioInfraction = ""

        /* Offender Information Reset */
        isPersonAbstent = true
        nameOffender = "QUIEN"
        lastFatherName = "RESULTE"
        lastMotherName = "RESPONSABLE"
        rfcOffenfer = ""
        stateOffender = States()
        townshipOffender = Townships()
        colonyOffender = ""
        streetOffender = ""
        noExtOffender = ""
        noIntOffender = ""
        noLicenseOffender = ""
        typeLicenseOffender = LicenseType(0, "")
        licenseIssuedInOffender = States()

        /* Calculated Variables Reset */
        idNewInfraction = 0L
        idNewPersonInfraction = 0L
        idPersonTownship = 0L
        subTotalInfraction = ""
        discountInfraction = ""
        totalInfraction = ""
        captureLineii = null
        captureLineiii = null
        amountCaptureLineii = 0F
        amountCaptureLineiii = 0F
        paymentAuthCode = ""
    }

    class DtoMotivation(var article: Articles, var fraction: InfractionFraction, var motivation: String)
}