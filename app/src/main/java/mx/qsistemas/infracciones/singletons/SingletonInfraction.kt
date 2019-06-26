package mx.qsistemas.infracciones.singletons

import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.net.catalogs.States

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

    var idStateOffender: Int = 0
    var idTownshipOffender: Int = 0
    var motivationList: MutableList<DtoMotivation> = mutableListOf()

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
    }

    class DtoMotivation(var article: Articles, var fraction: InfractionFraction, var motivation: String)
}