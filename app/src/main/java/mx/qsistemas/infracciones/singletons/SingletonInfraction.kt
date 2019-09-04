package mx.qsistemas.infracciones.singletons

import mx.qsistemas.infracciones.net.catalogs.*
import mx.qsistemas.infracciones.net.result_web.detail_result.CaptureLinesItem

object SingletonInfraction {

    /* Vehicle Information */
    var identifierDocument: GenericCatalog = GenericCatalog()
    var noDocument: String = ""
    var stateIssuedIn: GenericCatalog = GenericCatalog()
    var typeDocument: GenericCatalog = GenericCatalog()
    var brandVehicle: GenericCatalog = GenericCatalog()
    var subBrandVehicle: GenericSubCatalog = GenericSubCatalog()
    var colorVehicle: GenericCatalog = GenericCatalog()
    var typeVehicle: GenericCatalog = GenericCatalog()
    var noCirculationCard: String = ""
    var yearVehicle: String = ""
    var evidence1: String = ""
    var evidence2: String = ""
    var isNewSubBrand: Boolean = false
    var isNewColor: Boolean = false

    /* Infracion Information */
    var zipCodeInfraction: GenericSubCatalog = GenericSubCatalog()
    var colonnyInfraction: GenericSubCatalog = GenericSubCatalog()
    var streetInfraction: String = ""
    var betweenStreet1: String = ""
    var betweenStreet2: String = ""
    var motivationList: MutableList<DtoMotivation> = mutableListOf()
    var retainedDocument: GenericCatalog = GenericCatalog()
    var townshipInfraction: Townships = Townships()
    var stateInfraction: GenericCatalog = GenericCatalog()
    var isRemited: Boolean = false
    var dispositionRemited: GenericCatalog = GenericCatalog()
    var latitudeInfraction: Double = 0.0
    var longitudeInfraction: Double = 0.0
    var dateInfraction: String = ""
    var folioInfraction: String = ""

    /* Offender Information */
    var isPersonAbstent: Boolean = true
    var nameOffender: String = "Quien"
    var lastFatherName: String = "Resulte"
    var lastMotherName: String = "Responsable"
    var rfcOffenfer: String = ""
    var stateOffender: GenericCatalog = GenericCatalog()
    var townshipOffender: Townships = Townships()
    var zipCodeOffender: GenericSubCatalog = GenericSubCatalog()
    var colonyOffender: GenericSubCatalog = GenericSubCatalog()
    var streetOffender: String = ""
    var noExtOffender: String = ""
    var noIntOffender: String = ""
    var noLicenseOffender: String = ""
    var typeLicenseOffender: GenericCatalog = GenericCatalog()
    var licenseIssuedInOffender: GenericCatalog = GenericCatalog()

    /* Calculated Variables */
    var tokenInfraction: String = ""
    var idNewInfraction: Long = 0L
    var idNewPersonInfraction: Long = 0L
    var idOfficer: Long = 0L
    var subTotalInfraction: String = ""
    var discountInfraction: String = ""
    var totalInfraction: String = ""
    var surchargesInfraction: String = ""
    var paymentAuthCode: String = ""
    var captureLines: List<CaptureLinesItem?> = mutableListOf()

    fun cleanSingleton() {
        /* Vehicle Information Reset */
        identifierDocument = GenericCatalog()
        noDocument = ""
        stateIssuedIn = GenericCatalog()
        typeDocument = GenericCatalog()
        brandVehicle = GenericCatalog()
        subBrandVehicle = GenericSubCatalog()
        colorVehicle = GenericCatalog()
        typeVehicle = GenericCatalog()
        noCirculationCard = ""
        yearVehicle = ""
        evidence1 = ""
        evidence2 = ""
        isNewSubBrand = false
        isNewColor = false

        /* Infraction Information Reset */
        zipCodeInfraction = GenericSubCatalog()
        colonnyInfraction = GenericSubCatalog()
        streetInfraction = ""
        betweenStreet1 = ""
        betweenStreet2 = ""
        motivationList = mutableListOf()
        retainedDocument = GenericCatalog()
        isRemited = false
        dispositionRemited = GenericCatalog()
        latitudeInfraction = 0.0
        longitudeInfraction = 0.0
        dateInfraction = ""
        folioInfraction = ""

        /* Offender Information Reset */
        isPersonAbstent = true
        nameOffender = "Quien"
        lastFatherName = "Resulte"
        lastMotherName = "Responsable"
        rfcOffenfer = ""
        stateOffender = GenericCatalog()
        townshipOffender = Townships()
        zipCodeOffender = GenericSubCatalog()
        colonyOffender = GenericSubCatalog()
        streetOffender = ""
        noExtOffender = ""
        noIntOffender = ""
        noLicenseOffender = ""
        typeLicenseOffender = GenericCatalog()
        licenseIssuedInOffender = GenericCatalog()

        /* Calculated Variables Reset */
        tokenInfraction = ""
        idNewInfraction = 0L
        idNewPersonInfraction = 0L
        idOfficer = 0L
        subTotalInfraction = ""
        discountInfraction = ""
        totalInfraction = ""
        surchargesInfraction = ""
        paymentAuthCode = ""
        captureLines = mutableListOf()
    }

    class DtoMotivation(var article: Articles, var fraction: Fractions, var motivation: String)
}