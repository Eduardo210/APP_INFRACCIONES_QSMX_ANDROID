package mx.qsistemas.infracciones.singletons

object SingletonTicket {

    var headers: MutableList<String> = mutableListOf()

    var dateTicket: String = "-"
    var folioTicket: String = "-"

    var completeNameOffender: String = "Quien Resulte Responsable"
    var rfcOffender: String = ""
    var streetOffender: String = "-"
    var noExtOffender: String = "-"
    var noIntOffender: String = "-"
    var colonyOffender: String = "-"
    var stateOffender: String = "-"

    var completeNamePayer: String = ""
    var payerTaxDenomination: String = ""
    var payerRfc: String = ""
    var payerEmail: String = ""

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
    var idAgent: String = ""

    var paymentAuthCode: String = ""
    var captureLines: MutableList<CaptureLine> = mutableListOf()

    var footers: MutableList<String> = mutableListOf()

    class ArticleFraction(var article: String = "-", var fraction: String = "-", var umas: String = "-",
                          var motivation: String = "-")

    class CaptureLine(var captureLine: String = "0", var labelDiscount: String = "", var expirationDiscount: String = "",
                      var importInfraction: String = "")

    fun cleanData() {
        headers = mutableListOf()

        dateTicket = "-"
        folioTicket = "-"

        completeNameOffender = "Quien Resulte Responsable"
        rfcOffender = ""
        streetOffender = "-"
        noExtOffender = "-"
        noIntOffender = "-"
        colonyOffender = "-"
        stateOffender = "-"

        completeNamePayer = ""
        payerTaxDenomination = ""
        payerRfc = ""
        payerEmail = ""

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
        captureLines = mutableListOf()

        footers = mutableListOf()
    }
}