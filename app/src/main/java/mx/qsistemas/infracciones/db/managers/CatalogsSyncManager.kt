package mx.qsistemas.infracciones.db.managers

import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.net.catalogs.DownloadCatalogs
import java.util.concurrent.Executors

object CatalogsSyncManager {
    fun savePersonAttribute(personAttribute: MutableList<DownloadCatalogs.C_PersonAttribute>) {
        val map = mutableListOf<PersonAttibute>()
        personAttribute.forEach {
            map.add(PersonAttibute(0, it.idPersonAttribute.toInt(), it.idAttribute.toInt(),
                    it.idPerson.toLong()))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.personAttributeDao().deleteAll()
            Application.m_database.personAttributeDao().insertList(map)
        }
    }

    fun savePersonAccount(personAccount: MutableList<DownloadCatalogs.C_PersonAccount>) {
        val map = mutableListOf<PersonAccount>()
        personAccount.forEach {
            map.add(PersonAccount(it.idAccountUser.toLong(), it.idPerson.toLong(), it.isActive.toInt(), it.userName,
                    it.pswd))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.personAccountDao().insertList(map)
        }
    }

    fun saveAdscription(adscription: MutableList<DownloadCatalogs.C_Adscription>) {
        val map = mutableListOf<Ascription>()
        adscription.forEach {
            map.add(Ascription(it.idAdscription.toLong(), it.adscription, it.isVisible.toInt()))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.ascriptionDao().insertList(map)
        }
    }

    fun saveAttribute(attribute: MutableList<DownloadCatalogs.C_Attribute>) {
        val map = mutableListOf<Attribute>()
        attribute.forEach {
            map.add(Attribute(it.idAttribute.toLong(), it.idModule.toInt(), it.attribute))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.attributeDao().deleteAll()
            Application.m_database.attributeDao().insertList(map)
        }
    }

    fun saveColor(color: MutableList<DownloadCatalogs.C_Color>) {
        val map = mutableListOf<Colour>()
        color.forEach {
            map.add(Colour(it.idColor.toInt(), it.color))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.colourDao().insertList(map)
        }
    }

    fun saveConfiguration(configuration: MutableList<DownloadCatalogs.C_Configuration>) {
        val map = mutableListOf<Config>()
        configuration.forEach {
            map.add(Config(0, it.minimumSalary.toFloat(), it.daysDiscount.toInt(), it.discountRate.toFloat(),
                    it.header1, it.header2, it.header3, it.header4, it.header5, it.header6, it.foot1,
                    it.foot2, it.foot3, it.payDir1, it.payDir2, it.payDir3, it.payDir4, it.township,
                    it.idCountry, it.idState, it.idTownship))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.configDao().deleteAll()
            Application.m_database.configDao().insertList(map)
        }
    }

    fun saveNonWorkingDay(nonWorkingDay: MutableList<DownloadCatalogs.C_NonWorkingDay>) {
        val map = mutableListOf<NonWorkingDay>()
        nonWorkingDay.forEach {
            map.add(NonWorkingDay(it.idDay.toLong(), it.date))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.nonWorkingDayDao().insertList(map)
        }
    }

    fun saveState(state: MutableList<DownloadCatalogs.C_State>) {
        val map = mutableListOf<State>()
        state.forEach {
            map.add(State(it.idState.toInt(), it.idCountry.toInt(), it.state))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.stateDao().insertList(map)
        }
    }

    fun saveArticleInfraction(articleInfraction: MutableList<DownloadCatalogs.C_ArticleInfraction>) {
        val map = mutableListOf<Articles>()
        articleInfraction.forEach {
            map.add(Articles(it.id.toLong(), it.article, it.description))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.articlesDao().insertList(map)
        }
    }

    fun saveAuthorityExpedition(authorityExpedition: MutableList<DownloadCatalogs.C_AuthorityExpedition>) {
        val map = mutableListOf<AuthorityIssues>()
        authorityExpedition.forEach {
            map.add(AuthorityIssues(it.idAuthority.toInt(), it.authority))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.authorityIssuesDao().insertList(map)
        }
    }

    fun saveInfractionDisposition(infractionDisposition: MutableList<DownloadCatalogs.C_InfractionDisposition>) {
        val map = mutableListOf<Disposition>()
        infractionDisposition.forEach {
            map.add(Disposition(it.idDisposition.toInt(), it.disposition))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.dispositionDao().insertList(map)
        }
    }

    fun saveIdentifierDocument(identifierDocument: MutableList<DownloadCatalogs.C_IdentifierDocument>) {
        val map = mutableListOf<IdentifierDocument>()
        identifierDocument.forEach {
            map.add(IdentifierDocument(it.id.toInt(), it.document))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.identifierDocumentDao().insertList(map)
        }
    }

    fun saveRetainedDocument(retainedDocument: MutableList<DownloadCatalogs.C_RetainedDocument>) {
        val map = mutableListOf<RetainedDocument>()
        retainedDocument.forEach {
            map.add(RetainedDocument(it.idDocument.toInt(), it.document))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.retainedDocumentDao().insertList(map)
        }
    }

    fun saveFractionInfraction(fractionInfraction: MutableList<DownloadCatalogs.C_FractionInfraction>) {
        val map = mutableListOf<InfractionFraction>()
        fractionInfraction.forEach {
            map.add(InfractionFraction(it.id.toLong(), it.idArticle.toInt(), it.fraction, it.description,
                    it.ticket, it.minimumSalary.toInt(), it.penaltyPoints.toInt()))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.infractionFractionDao().insertList(map)
        }
    }

    fun saveTypeLicense(typeLicense: MutableList<DownloadCatalogs.C_TypeLicense>) {
        val map = mutableListOf<LicenseType>()
        typeLicense.forEach {
            map.add(LicenseType(it.id.toInt(), it.typeLicense))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.licenseTypeDao().insertList(map)
        }
    }

    fun saveTypeVehicle(typeVehicle: MutableList<DownloadCatalogs.C_TypeVehicle>) {
        val map = mutableListOf<VehicleType>()
        typeVehicle.forEach {
            map.add(VehicleType(it.idType.toLong(), it.type))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.vehicleTypeDao().insertList(map)
        }
    }

    fun saveBrandVehicle(brandVehicle: MutableList<DownloadCatalogs.C_BrandVehicle>) {
        val map = mutableListOf<VehicleBrand>()
        brandVehicle.forEach {
            map.add(VehicleBrand(it.idBrandVehicle.toInt(), it.brandVehicle))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.vehicleBrandDao().insertList(map)
        }
    }

    fun saveModule(module: MutableList<DownloadCatalogs.C_Module>) {
        val map = mutableListOf<Module>()
        module.forEach {
            map.add(Module(it.idModule.toInt(), it.module))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.moduleDao().deleteAll()
            Application.m_database.moduleDao().inserList(map)
        }
    }

    fun saveSepomex(townshipSepomex: MutableList<DownloadCatalogs.C_TownshipSepomex>) {
        val map = mutableListOf<TownSepoMex>()
        townshipSepomex.forEach {
            map.add(TownSepoMex(it.idTownship.toInt(), it.idState.toInt(), it.township))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.townSepomexDao().insertList(map)
        }
    }

    fun savePerson(person: MutableList<DownloadCatalogs.C_Person>) {
        val map = mutableListOf<Person>()
        person.forEach {
            map.add(Person(it.idPerson.toInt(), it.name, it.fatherLastName, it.motherLastName, ""))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.personDao().insertList(map)
        }
    }

    fun saveTownhallPerson(townhallPerson: MutableList<DownloadCatalogs.C_TownhallPerson>) {
        val map = mutableListOf<PersonTownship>()
        townhallPerson.forEach {
            map.add(PersonTownship(it.idTownhallPerson.toInt(), it.idPerson.toLong(), it.employee))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.personTownshipDao().insertList(map)
        }
    }

    fun saveSubBrandVehicle(subBrandVehicle: MutableList<DownloadCatalogs.C_SubBrandVehicle>) {
        val map = mutableListOf<SubmarkingVehicle>()
        subBrandVehicle.forEach {
            map.add(SubmarkingVehicle(it.idSubBrandvehicle.toInt(), it.idBrandVehicle.toInt(),
                    it.subBrandVehicle))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.submarkingVehicleDao().insertList(map)
        }
    }

    fun saveSynch(synchonization: MutableList<DownloadCatalogs.C_Synchonization>) {
        val map = mutableListOf<Syncronization>()
        synchonization.forEach {
            map.add(Syncronization(0, it.activeApplication.toInt()))
        }
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.synchronizationDao().deleteAll()
            Application.m_database.synchronizationDao().insertList(map)
        }
    }

}