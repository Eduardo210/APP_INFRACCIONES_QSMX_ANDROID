package mx.qsistemas.infracciones.net.catalogs

import com.google.gson.annotations.SerializedName

data class DownloadCatalogs(@SerializedName("Flag") val flag: Boolean,
                            @SerializedName("ultimaFechaSincronizacion") val lastUpdate: String,
                            @SerializedName("SIIPTA_PERSONA_ATRIBUTO") val personAttribute: MutableList<PersonAttribute>,
                            @SerializedName("SIIPTA_PERSONA_CUENTA") val personAccount: MutableList<PersonAccount>,
                            @SerializedName("SIIPTC_ADSCRIPCION") val adscription: MutableList<Adscription>,
                            @SerializedName("SIIPTC_ATRIBUTO") val attribute: MutableList<Attribute>,
                            @SerializedName("SIIPTC_COLOR") val color: MutableList<Color>,
                            @SerializedName("SIIPTC_CONFIGURACION") val configuration: MutableList<Configuration>,
                            @SerializedName("SIIPTC_DIA_NO_HABIL") val nonWorkingDay: MutableList<NonWorkingDay>,
                            @SerializedName("SIIPTC_ESTADO") val state: MutableList<State>,
                            @SerializedName("SIIPTC_INFRACCION_ARTICULO") val articleInfraction: MutableList<ArticleInfraction>,
                            @SerializedName("SIIPTC_INFRACCION_AUTORIDAD_EXPIDE") val authorityExpedition: MutableList<AuthorityExpedition>,
                            @SerializedName("SIIPTC_INFRACCION_DISPOSICION") val infractionDisposition: MutableList<InfractionDisposition>,
                            @SerializedName("SIIPTC_INFRACCION_DOCUMENTO_IDENTIFICADOR") val identifierDocument: MutableList<IdentifierDocument>,
                            @SerializedName("SIIPTC_INFRACCION_DOCUMENTO_RETENIDO") val retainedDocument: MutableList<RetainedDocument>,
                            @SerializedName("SIIPTC_INFRACCION_FRACCION") val fractionInfraction: MutableList<FractionInfraction>,
                            @SerializedName("SIIPTC_INFRACCION_TIPO_LICENCIA") val typeLicense: MutableList<TypeLicense>,
                            @SerializedName("SIIPTC_INFRACCION_TIPO_VEHICULO") val typeVehicle: MutableList<TypeVehicle>,
                            @SerializedName("SIIPTC_MARCA_VEHICULO") val brandVehicle: MutableList<BrandVehicle>,
                            @SerializedName("SIIPTC_MODULO") val module: MutableList<Module>,
                            @SerializedName("SIIPTC_MUNICIPIO_SEPOMEX") val townshipSepomex: MutableList<TownshipSepomex>,
                            @SerializedName("SIIPTC_PERSONA") val person: MutableList<Person>,
                            @SerializedName("SIIPTC_PERSONA_AYUNTAMIENTO") val townhallPerson: MutableList<TownhallPerson>,
                            @SerializedName("SIIPTC_SUBMARCA_VEHICULO") val subBrandVehicle: MutableList<SubBrandVehicle>,
                            @SerializedName("SIIPTC_SYNCRONIZACION") val synchonization: MutableList<Synchonization>) {

    data class PersonAttribute(@SerializedName("ID_PERSONA_ATRIBUTO") val idPersonAttribute: String,
                               @SerializedName("ID_PERSONA") val idPerson: String,
                               @SerializedName("ID_ATRIBUTO") val idAttribute: String)

    data class PersonAccount(@SerializedName("ID_CUENTA_USUARIO") val idAccountUser: String,
                             @SerializedName("ID_PERSONA") val idPerson: String,
                             @SerializedName("BAN_ACTIVA") val isActive: String,
                             @SerializedName("USER_NAME") val userName: String,
                             @SerializedName("PASSWORD") val pswd: String)

    data class Adscription(@SerializedName("ID_ADSCRIPCION") val idAdscription: String,
                           @SerializedName("ADSCRIPCION") val adscription: String,
                           @SerializedName("BAN_VISIBLE") val isVisible: String)

    data class Attribute(@SerializedName("ID_ATRIBUTO") val idAttribute: String,
                         @SerializedName("ID_MODULO") val idModule: String,
                         @SerializedName("ATRIBUTO") val attribute: String)

    data class Color(@SerializedName("ID_COLOR") val idColor: String,
                     @SerializedName("COLOR") val color: String)

    data class Configuration(@SerializedName("SALARIO_MINIMO") val minimumSalary: String,
                             @SerializedName("PLAZO_DIAS_DESCUENTO_INFRACCION") val daysDiscount: String,
                             @SerializedName("TAZA_DESCUENTO_INFRACCION_MISMO_DIA") val discountRate: String,
                             @SerializedName("INFRACCION_IMPRESION_ENCABEZADO_I") val header1: String,
                             @SerializedName("INFRACCION_IMPRESION_ENCABEZADO_II") val header2: String,
                             @SerializedName("INFRACCION_IMPRESION_ENCABEZADO_III") val header3: String,
                             @SerializedName("INFRACCION_IMPRESION_ENCABEZADO_IV") val header4: String,
                             @SerializedName("INFRACCION_IMPRESION_ENCABEZADO_V") val header5: String,
                             @SerializedName("INFRACCION_IMPRESION_ENCABEZADO_VI") val header6: String,
                             @SerializedName("INFRACCION_IMPRESION_PIE_I") val foot1: String,
                             @SerializedName("INFRACCION_IMPRESION_PIE_II") val foot2: String,
                             @SerializedName("INFRACCION_IMPRESION_PIE_III") val foot3: String,
                             @SerializedName("INFRACCION_IMPRESION_DIR_PAGO_I") val payDir1: String,
                             @SerializedName("INFRACCION_IMPRESION_DIR_PAGO_II") val payDir2: String,
                             @SerializedName("INFRACCION_IMPRESION_DIR_PAGO_III") val payDir3: String,
                             @SerializedName("INFRACCION_IMPRESION_DIR_PAGO_IV") val payDir4: String,
                             @SerializedName("INFRACCION_MUNICIPIO") val township: String,
                             @SerializedName("INFRACCION_ID_PAIS") val idCountry: Int,
                             @SerializedName("INFRACCION_ID_ENTIDAD") val idState: Int,
                             @SerializedName("INFRACCION_ID_MUNICIPIO") val idTownship: Int)

    data class NonWorkingDay(@SerializedName("ID_DIA") val idDay: String,
                             @SerializedName("FECHA") val date: String)

    data class State(@SerializedName("ID_ESTADO") val idState: String,
                     @SerializedName("ID_PAIS") val idCountry: String,
                     @SerializedName("ESTADO") val state: String)

    data class ArticleInfraction(@SerializedName("ID") val id: String,
                                 @SerializedName("ARTICULO") val article: String,
                                 @SerializedName("DESCRIPCION") val description: String)

    data class AuthorityExpedition(@SerializedName("ID_AUTORIDAD") val idAuthority: String,
                                   @SerializedName("AUTORIDAD") val authority: String)

    data class InfractionDisposition(@SerializedName("ID_DISPOSICION") val idDisposition: String,
                                     @SerializedName("DISPOSICION") val disposition: String)

    data class IdentifierDocument(@SerializedName("ID") val id: String,
                                  @SerializedName("DOCUMENTO") val document: String)

    data class RetainedDocument(@SerializedName("ID_DOCUMENTO") val idDocument: String,
                                @SerializedName("DOCUMENTO") val document: String)

    data class FractionInfraction(@SerializedName("ID") val id: String,
                                  @SerializedName("ID_ARTICULO") val idArticle: String,
                                  @SerializedName("FRACCION") val fraction: String,
                                  @SerializedName("DESCRIPCION") val description: String,
                                  @SerializedName("ALIAS_TICKET") val ticket: String,
                                  @SerializedName("SALARIOS_MINIMO") val minimumSalary: String,
                                  @SerializedName("PUNTOS_SANCION") val penaltyPoints: String)

    data class TypeLicense(@SerializedName("ID") val id: String,
                           @SerializedName("TIPO_LICENCIA") val typeLicense: String)

    data class TypeVehicle(@SerializedName("ID_TIPO") val idType: String,
                           @SerializedName("TIPO") val type: String)

    data class BrandVehicle(@SerializedName("ID_MARCA_VEHICULO") val idBrandVehicle: String,
                            @SerializedName("MARCA_VEHICULO") val brandVehicle: String)

    data class Module(@SerializedName("ID_MODULO") val idModule: String,
                      @SerializedName("MODULO") val module: String)

    data class TownshipSepomex(@SerializedName("ID_MUNICIPIO") val idTownship: String,
                               @SerializedName("ID_ESTADO") val idState: String,
                               @SerializedName("MUNICIPIO") val township: String)

    data class Person(@SerializedName("ID_PERSONA") val idPerson: String,
                      @SerializedName("NOMBRE") val name: String,
                      @SerializedName("A_PATERNO") val fatherLastName: String,
                      @SerializedName("A_MATERNO") val motherLastName: String)

    data class TownhallPerson(@SerializedName("ID_PERSONA_AYUNTAMIENTO") val idTownhallPerson: String,
                              @SerializedName("ID_PERSONA") val idPerson: String,
                              @SerializedName("EMPLEADO") val employee: String)

    data class SubBrandVehicle(@SerializedName("ID_SUBMARCA_VEHICULO") val idSubBrandvehicle: String,
                               @SerializedName("ID_MARCA_VEHICULO") val idBrandVehicle: String,
                               @SerializedName("SUBMARCA_VEHICULO") val subBrandVehicle: String)

    data class Synchonization(@SerializedName("APLICACION_ACTIVA") val activeApplication: String)
}