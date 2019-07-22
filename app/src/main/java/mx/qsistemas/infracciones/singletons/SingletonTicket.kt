package mx.qsistemas.infracciones.singletons

object SingletonTicket {

    var dateTicket: String = "-"
    var folioTicket: String = "-"

    var completeNameOffender: String = "QUIEN RESULTE RESPONSABLE"
    var rfcOffender: String = ""
    var streetOffender: String = "-"
    var noExtOffender: String = "-"
    var noIntOffender: String = "-"
    var colonyOffender: String = "-"
    var stateOffender: String = "-"

    var noLicenseOffender: String = "-"
    var typeLicenseOffender: String = "-"
    var stateLicenseOffender: String = "-"

    var brandVehicle: String = "-"
    var subBrandVehicle: String = "-"
    var typeVehicle: String = "-"
    var colorVehicle: String = "-"
    var modelVehicle: String = "-"
    var identifierVehicle: String = "-"
    var noIdentifierVehicle: String = "-"
    var expeditionAuthVehicle: String = "-"
    var stateExpVehicle: String = "-"

    var fractionsList: MutableList<ArticleFraction> = mutableListOf()

    var streetInfraction: String = "-"
    var betweenStreetInfraction: String = "-"
    var andStreetInfraction: String = "-"
    var colonyInfraction: String = "-"

    var retainedDocumentInfraction: String = "NINGUNO"
    var isRemitedInfraction: Boolean = false
    var remitedDispositionInfraction: String = ""
    var nameAgent: String = ""

    var paymentAuthCode: String = ""
    var captureLineList: MutableList<CaptureLine> = mutableListOf()

    class ArticleFraction(var article: String = "-", var fraction: String = "-", var umas: String = "-",
                          var points: String = "-", var motivation: String = "-")

    class CaptureLine(var captureLine: String = "0", var labelDiscount: String = "", var expirationDiscount: String = "",
                      var importInfraction: String = "")

    fun cleanData() {
        dateTicket = "-"
        folioTicket = "-"

        completeNameOffender = "QUIEN RESULTE RESPONSABLE"
        rfcOffender = ""
        streetOffender = "-"
        noExtOffender = "-"
        noIntOffender = "-"
        colonyOffender = "-"
        stateOffender = "-"

        noLicenseOffender = "-"
        typeLicenseOffender = "-"
        stateLicenseOffender = "-"

        brandVehicle = "-"
        subBrandVehicle = "-"
        typeVehicle = "-"
        colorVehicle = "-"
        modelVehicle = "-"
        identifierVehicle = "-"
        noIdentifierVehicle = "-"
        expeditionAuthVehicle = "-"
        stateExpVehicle = "-"

        fractionsList = mutableListOf()

        streetInfraction = "-"
        betweenStreetInfraction = "-"
        andStreetInfraction = "-"
        colonyInfraction = "-"

        retainedDocumentInfraction = "NINGUNO"
        isRemitedInfraction = false
        remitedDispositionInfraction = ""
        nameAgent = ""

        paymentAuthCode = ""
        captureLineList = mutableListOf()
    }
}