package mx.qsistemas.infracciones.singletons

import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships
import java.util.*

object SingletonInfraction {

    /* Vehicle Information */
    var identifierDocument: IdentifierDocument = IdentifierDocument(0, "")
    var noDocument: String = ""
    var stateIssuedIn: States = States()
    var typeDocument: AuthorityIssues = AuthorityIssues(0, "")
    var brandVehicle: VehicleBrand = VehicleBrand(0, "")
    var subBrandVehicle: String = ""
    var colorVehicle: String = ""
    var typeVehicle: VehicleType = VehicleType(0, "")
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

    /* Offender Information */
    var isPersonAbstent: Boolean = true
    var nameOffender: String = "QUIEN"
    var lastFatherName: String = "RESULTE"
    var lastMotherName: String = "RESPONSABLE"
    var rfcOffenfer: String = ""
    var stateOffender: States = States()
    var townshipOffender: Townships = Townships()
    var colonyOffender: String = ""
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
    var captureLineiii: Date?=null
    var amountCaptureLineii:Float = 0.0f
    var amountCaptureLineiii:Float = 0.0f

    fun cleanSingleton() {
        /* Vehicle Information Reset */
        identifierDocument = IdentifierDocument(0, "")
        noDocument = ""
        stateIssuedIn = States()
        typeDocument = AuthorityIssues(0, "")
        brandVehicle = VehicleBrand(0, "")
        subBrandVehicle = ""
        colorVehicle = ""
        typeVehicle = VehicleType(0, "")
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

        /* Offender Information Reset */
        isPersonAbstent = true
        nameOffender = "QUIEN"
        lastFatherName = "RESULTE"
        lastMotherName = "RESPONSABLE"
        rfcOffenfer = ""
        stateOffender = States()
        townshipOffender = Townships()
        colonyOffender = ""
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
        totalInfraction = ""
        discountInfraction = ""
    }

    class DtoMotivation(var article: Articles, var fraction: InfractionFraction, var motivation: String)
}