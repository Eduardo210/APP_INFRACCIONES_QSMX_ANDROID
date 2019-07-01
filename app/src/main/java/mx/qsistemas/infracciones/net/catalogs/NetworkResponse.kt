package mx.qsistemas.infracciones.net.catalogs

import com.google.gson.annotations.SerializedName

data class DownloadCatalogs(@SerializedName("Flag") val flag: Boolean,
                            @SerializedName("ultimaFechaSincronizacion") val lastUpdate: String,
                            @SerializedName("SIIPTA_PERSONA_ATRIBUTO") val personAttribute: MutableList<C_PersonAttribute>,
                            @SerializedName("SIIPTA_PERSONA_CUENTA") val personAccount: MutableList<C_PersonAccount>,
                            @SerializedName("SIIPTC_ADSCRIPCION") val adscription: MutableList<C_Adscription>,
                            @SerializedName("SIIPTC_ATRIBUTO") val attribute: MutableList<C_Attribute>,
                            @SerializedName("SIIPTC_COLOR") val color: MutableList<C_Color>,
                            @SerializedName("SIIPTC_CONFIGURACION") val configuration: MutableList<C_Configuration>,
                            @SerializedName("SIIPTC_DIA_NO_HABIL") val nonWorkingDay: MutableList<C_NonWorkingDay>,
                            @SerializedName("SIIPTC_ESTADO") val state: MutableList<C_State>,
                            @SerializedName("SIIPTC_INFRACCION_ARTICULO") val articleInfraction: MutableList<C_ArticleInfraction>,
                            @SerializedName("SIIPTC_INFRACCION_AUTORIDAD_EXPIDE") val authorityExpedition: MutableList<C_AuthorityExpedition>,
                            @SerializedName("SIIPTC_INFRACCION_DISPOSICION") val infractionDisposition: MutableList<C_InfractionDisposition>,
                            @SerializedName("SIIPTC_INFRACCION_DOCUMENTO_IDENTIFICADOR") val identifierDocument: MutableList<C_IdentifierDocument>,
                            @SerializedName("SIIPTC_INFRACCION_DOCUMENTO_RETENIDO") val retainedDocument: MutableList<C_RetainedDocument>,
                            @SerializedName("SIIPTC_INFRACCION_FRACCION") val fractionInfraction: MutableList<C_FractionInfraction>,
                            @SerializedName("SIIPTC_INFRACCION_TIPO_LICENCIA") val typeLicense: MutableList<C_TypeLicense>,
                            @SerializedName("SIIPTC_INFRACCION_TIPO_VEHICULO") val typeVehicle: MutableList<C_TypeVehicle>,
                            @SerializedName("SIIPTC_MARCA_VEHICULO") val brandVehicle: MutableList<C_BrandVehicle>,
                            @SerializedName("SIIPTC_MODULO") val module: MutableList<C_Module>,
                            @SerializedName("SIIPTC_MUNICIPIO_SEPOMEX") val townshipSepomex: MutableList<C_TownshipSepomex>,
                            @SerializedName("SIIPTC_PERSONA") val person: MutableList<C_Person>,
                            @SerializedName("SIIPTC_PERSONA_AYUNTAMIENTO") val townhallPerson: MutableList<C_TownhallPerson>,
                            @SerializedName("SIIPTC_SUBMARCA_VEHICULO") val subBrandVehicle: MutableList<C_SubBrandVehicle>,
                            @SerializedName("SIIPTC_SYNCRONIZACION") val synchonization: MutableList<C_Synchonization>) {

    data class C_PersonAttribute(@SerializedName("ID_PERSONA_ATRIBUTO") val idPersonAttribute: String,
                                 @SerializedName("ID_PERSONA") val idPerson: String,
                                 @SerializedName("ID_ATRIBUTO") val idAttribute: String)

    data class C_PersonAccount(@SerializedName("ID_CUENTA_USUARIO") val idAccountUser: String,
                               @SerializedName("ID_PERSONA") val idPerson: String,
                               @SerializedName("BAN_ACTIVA") val isActive: String,
                               @SerializedName("USER_NAME") val userName: String,
                               @SerializedName("PASSWORD") val pswd: String)

    data class C_Adscription(@SerializedName("ID_ADSCRIPCION") val idAdscription: String,
                             @SerializedName("ADSCRIPCION") val adscription: String,
                             @SerializedName("BAN_VISIBLE") val isVisible: String)

    data class C_Attribute(@SerializedName("ID_ATRIBUTO") val idAttribute: String,
                           @SerializedName("ID_MODULO") val idModule: String,
                           @SerializedName("ATRIBUTO") val attribute: String)

    data class C_Color(@SerializedName("ID_COLOR") val idColor: String,
                       @SerializedName("COLOR") val color: String)

    data class C_Configuration(@SerializedName("SALARIO_MINIMO") val minimumSalary: String,
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

    data class C_NonWorkingDay(@SerializedName("ID_DIA") val idDay: String,
                               @SerializedName("FECHA") val date: String)

    data class C_State(@SerializedName("ID_ESTADO") val idState: String,
                       @SerializedName("ID_PAIS") val idCountry: String,
                       @SerializedName("ESTADO") val state: String)

    data class C_ArticleInfraction(@SerializedName("ID") val id: String,
                                   @SerializedName("ARTICULO") val article: String,
                                   @SerializedName("DESCRIPCION") val description: String)

    data class C_AuthorityExpedition(@SerializedName("ID_AUTORIDAD") val idAuthority: String,
                                     @SerializedName("AUTORIDAD") val authority: String)

    data class C_InfractionDisposition(@SerializedName("ID_DISPOSICION") val idDisposition: String,
                                       @SerializedName("DISPOSICION") val disposition: String)

    data class C_IdentifierDocument(@SerializedName("ID") val id: String,
                                    @SerializedName("DOCUMENTO") val document: String)

    data class C_RetainedDocument(@SerializedName("ID_DOCUMENTO") val idDocument: String,
                                  @SerializedName("DOCUMENTO") val document: String)

    data class C_FractionInfraction(@SerializedName("ID") val id: String,
                                    @SerializedName("ID_ARTICULO") val idArticle: String,
                                    @SerializedName("FRACCION") val fraction: String,
                                    @SerializedName("DESCRIPCION") val description: String,
                                    @SerializedName("ALIAS_TICKET") val ticket: String,
                                    @SerializedName("SALARIOS_MINIMO") val minimumSalary: String,
                                    @SerializedName("PUNTOS_SANCION") val penaltyPoints: String)

    data class C_TypeLicense(@SerializedName("ID") val id: String,
                             @SerializedName("TIPO_LICENCIA") val typeLicense: String)

    data class C_TypeVehicle(@SerializedName("ID_TIPO") val idType: String,
                             @SerializedName("TIPO") val type: String)

    data class C_BrandVehicle(@SerializedName("ID_MARCA_VEHICULO") val idBrandVehicle: String,
                              @SerializedName("MARCA_VEHICULO") val brandVehicle: String)

    data class C_Module(@SerializedName("ID_MODULO") val idModule: String,
                        @SerializedName("MODULO") val module: String)

    data class C_TownshipSepomex(@SerializedName("ID_MUNICIPIO") val idTownship: String,
                                 @SerializedName("ID_ESTADO") val idState: String,
                                 @SerializedName("MUNICIPIO") val township: String)

    data class C_Person(@SerializedName("ID_PERSONA") val idPerson: String,
                        @SerializedName("NOMBRE") val name: String,
                        @SerializedName("A_PATERNO") val fatherLastName: String,
                        @SerializedName("A_MATERNO") val motherLastName: String)

    data class C_TownhallPerson(@SerializedName("ID_PERSONA_AYUNTAMIENTO") val idTownhallPerson: String,
                                @SerializedName("ID_PERSONA") val idPerson: String,
                                @SerializedName("EMPLEADO") val employee: String)

    data class C_SubBrandVehicle(@SerializedName("ID_SUBMARCA_VEHICULO") val idSubBrandvehicle: String,
                                 @SerializedName("ID_MARCA_VEHICULO") val idBrandVehicle: String,
                                 @SerializedName("SUBMARCA_VEHICULO") val subBrandVehicle: String)

    data class C_Synchonization(@SerializedName("APLICACION_ACTIVA") val activeApplication: String)
}

data class InfractionList(@SerializedName("Flag") val flag: Boolean,
                          @SerializedName("Mensaje") val message: String,
                          @SerializedName("Resultados") val results: MutableList<Results>) {

    data class Results(@SerializedName("id_infraction") val id_infraction: Long,
                       @SerializedName("id_doc_ident") val id_doc_ident: Int,
                       @SerializedName("title_vehicle") val title_vehicle: String,
                       @SerializedName("folio") val folio: String,
                       @SerializedName("motivation") val motivation: String,
                       @SerializedName("identifier_document") val identifier_document: String,
                       @SerializedName("date_infra") val date_infra: String,
                       @SerializedName("it_is_paid") val it_is_paid: Int)
}

data class InfractionSearch(@SerializedName("Flag") val flag: Boolean,
                            @SerializedName("InfraccionGuardar") val save: Int,
                            @SerializedName("InfraccionFolio") val folio: String,
                            @SerializedName("InfraccionFecha") val date: String,
                            @SerializedName("InfraccionMotivo") val reason: String,
                            @SerializedName("InfraccionBanRemitido") val referred: Int,
                            @SerializedName("InfraccionDocumentoRetenido") val retained_document: String,
                            @SerializedName("InfraccionSalariosMinimosTotales") val total_minimum_wages: Int,
                            @SerializedName("InfraccionImporteTotal") val total: Float,
                            @SerializedName("InfraccionTazaSalarioMinimo") val minimum_wage: Float,
                            @SerializedName("InfraccionBanPagada") val is_paid: Int,
                            @SerializedName("InfraccionIdFuente") val source: Int,
                            @SerializedName("InfraccionIdSector") val sector: Int,
                            @SerializedName("InfraccionIdOficial") val id_official: Long,
                            @SerializedName("InfraccionOficial") val official: String,
                            @SerializedName("InfraccionIdEstatus") val status: Int,
                            @SerializedName("InfraccionNumHojasExpediente") val num_sheets: Int,
                            @SerializedName("InfraccionAutoridadExpide") val authority_issue: String,
                            @SerializedName("InfraccionIdAutoridadExpidePlaca") val issues_vehicular_plate: Int,
                            @SerializedName("InfraccionFolioEvidencia") val folio_evidence: String,
                            @SerializedName("InfraccionIdDisposicion") val disposition: Int,
                            @SerializedName("InfraccionBanInfractorAusente") val is_absent: Int,
                            @SerializedName("InfraccionPuntosSancion") val penalty_points: Int,
                            @SerializedName("InfraccionLineaCapturaI") val capture_line_i: String,
                            @SerializedName("InfraccionLineaCapturaII") val capture_line_ii: String,
                            @SerializedName("InfraccionLineaCapturaIII") val capture_line_iii: String,
                            @SerializedName("InfraccionFechaLineaCapturaI") val date_capture_line_i: String,
                            @SerializedName("InfraccionFechaLineaCapturaII") val date_capture_line_ii: String,
                            @SerializedName("InfraccionFechaLineaCapturaIII") val date_capture_line_iii: String,
                            @SerializedName("InfraccionImporteLineaCapturaI") val amount_capture_line_i: Float,
                            @SerializedName("InfraccionImporteLineaCapturaII") val amount_capture_line_ii: Float,
                            @SerializedName("InfraccionImporteLineaCapturaIII") val amount_capture_line_iii: Float,
                            @SerializedName("InfraccionDomicilioIdPais") val id_address_country: Int,
                            @SerializedName("InfraccionDomicilioIdEstado") val id_address_state: Int,
                            @SerializedName("InfraccionDomicilioIdMunicipio") val id_address_township: Int,
                            @SerializedName("InfraccionDomicilioIdColonia") val id_address_colony: Int,
                            @SerializedName("InfraccionDomicilioColonia") val address_colony: String,
                            @SerializedName("InfraccionDomicilioIdCalle") val id_address_street: Int,
                            @SerializedName("InfraccionDomicilioCalle") val address_street: String,
                            @SerializedName("InfraccionDomicilioIdEntreCalle") val id_address_between_street: Int,
                            @SerializedName("InfraccionDomicilioEntreCalle") val address_between_street: String,
                            @SerializedName("InfraccionDomicilioIdYCalle") val id_address_and_street: Int,
                            @SerializedName("InfraccionDomicilioYCalle") val address_and_street: String,
                            @SerializedName("InfraccionDomicilioX") val address_x: Double,
                            @SerializedName("InfraccionDomicilioY") val address_y: Double,
                            @SerializedName("InfraccionFracciones") val infraction_fraction: MutableList<Fractions>,
                            @SerializedName("InfractorNombre") val name: String,
                            @SerializedName("InfractorPaterno") val last_name: String,
                            @SerializedName("InfractorMaterno") val mother_last_name: String,
                            @SerializedName("InfractorRfc") val rfc: String,
                            @SerializedName("InfractorNumPermisoLicencia") val license_number: String,
                            @SerializedName("InfractorIdTipoLicencia") val id_license_type: Int,
                            @SerializedName("InfractorLicenciaExpedidaEn") val issued_in: String,
                            @SerializedName("InfractorDomicilioExiste") val is_there_address: Boolean,
                            @SerializedName("InfractorDomicilioIdPais") val infractor_id_country: Int,
                            @SerializedName("InfractorDomicilioIdEstado") val infraction_id_state: Int,
                            @SerializedName("InfractorDomicilioIdMunicipioSEPOMEX") val infractor_id_township: Int,
                            @SerializedName("InfractorDomicilioMunicipioSEPOMEX") val infractor_township: String,
                            @SerializedName("InfractorDomicilioIdColonia") val infractor_id_colony: Int,
                            @SerializedName("InfractorDomicilioColonia") val infractor_colony: String,
                            @SerializedName("InfractorDomicilioIdCalle") val infractor_id_street: Int,
                            @SerializedName("InfractorDomicilioCalle") val infractor_street: String,
                            @SerializedName("InfractorDomicilioNumeroExterior") val infractor_external_number: String,
                            @SerializedName("InfractorDomicilioNumeroInterior") val infractor_internal_number: String,
                            @SerializedName("VehiculoIdMarca") val id_brand: Int,
                            @SerializedName("Marca") val brand: String,
                            @SerializedName("VehiculoSubMarca") val sub_brand: String,
                            @SerializedName("VehiculoTipo") val vehicle_type: String,
                            @SerializedName("VehiculoColor") val vehicle_color: String,
                            @SerializedName("VehiculoModelo") val vehicle_model: String,
                            @SerializedName("VehiculoIdDocumentoIdentificador") val id_doc_ident: Int,
                            @SerializedName("VehiculoDocumentoIdentificador") val ident_document: String,
                            @SerializedName("VehiculoNumeroDocumentoIdentificador") val num_doc_ident: String,
                            @SerializedName("VehiculoDocumentoIdentificadorExpedidoEn") val doc_ident_issued: String,
                            @SerializedName("VehiculoBarCodePlaca") val barcode_vehicular_plate: String,
                            @SerializedName("VehiculoTarjetaCirculacion") val circulation_card: String,
                            @SerializedName("PagoTarjetaNumeroAutorizacion") val authorization_number: String,
                            @SerializedName("PagoTarjetaAID") val card_aid: String,
                            @SerializedName("PagoTarjetaAPPLabel") val card_app_label: String,
                            @SerializedName("PagoTarjetaARQC") val card_arqc: String,
                            @SerializedName("PagoTarjetaEntryType") val card_entry_type: String,
                            @SerializedName("PagoTarjetaMaskedPAN") val card_masked_pan: String,
                            @SerializedName("PagoTarjetaTrxDate") val card_trx_date: String,
                            @SerializedName("PagoTarjetaTrxNb") val card_trx_nb: String,
                            @SerializedName("PagoTarjetaTrxTime") val card_trx_time: String,
                            @SerializedName("PagoTarjetaSerieMovil") val card_serie_movil: String,
                            @SerializedName("PagoTarjetaAfiliacion") val card_membership: String,
                            @SerializedName("PagoTarjetaVigenciaTarjeta") val card_expiration: String,
                            @SerializedName("PagoTarjetaMensaje") val card_message: String,
                            @SerializedName("PagoTarjetaTipoTarjeta") val card_type_type: String,
                            @SerializedName("PagoTarjetaTipo") val card_type: String,
                            @SerializedName("PagoTarjetaBancoEmisor") val card_issuing_bank: String,
                            @SerializedName("PagoTarjetaReferencia") val card_reference: String,
                            @SerializedName("PagoTarjetaImporte") val card_import: String,
                            @SerializedName("PagoTarjetaTVR") val card_tvr: String,
                            @SerializedName("PagoTarjetaTSI") val card_tsi: String,
                            @SerializedName("PagoTarjetaNumeroControl") val card_control_number: String,
                            @SerializedName("PagoTarjetaTarjetaHabiente") val card_cardholder: String,
                            @SerializedName("PagoTarjetaEMV_Data") val car_emv_data: String,
                            @SerializedName("PagoTarjetaTipoTransaccion") val car_transaction_type: String,
                            @SerializedName("PagoInfraccionIdFormaPago") val way_to_pay: Int,
                            @SerializedName("PagoInfraccionSubtotal") val subtotal: Float,
                            @SerializedName("PagoInfraccionDescuento") val payment_discount: Float,
                            @SerializedName("PagoInfraccionTotal") val payment_total: Float,
                            @SerializedName("PagoInfraccionObservacion") val payment_observation: String,
                            @SerializedName("PagoInfraccionRecargos") val payment_surcharges: Float,
                            @SerializedName("SincronizarIdUsuario") val sync_id_user: Long,
                            @SerializedName("Empleado") val employee: String
) {

    data class Fractions(@SerializedName("FraccionIdFraccion") val id_fraction: Int,
                         @SerializedName("ArticuloFraccion") val art_fracc: String,
                         @SerializedName("FraccionSalariosMinimos") val minimum_wages: Int,
                         @SerializedName("FraccionPuntosSancion") val penalty_points: Int,
                         @SerializedName("FraccionMotivacion") val motivation: String?)
}

data class UpdatePerson(@SerializedName("Flag") val flag: Boolean,
                        @SerializedName("Mensaje") val message: String,
                        @SerializedName("Ids") val ids: MutableList<Int>)

data class SendInfractionResponse(@SerializedName("Flag") val flag: Boolean,
                                  @SerializedName("Mensaje") val message: String,
                                  @SerializedName("Ids") val folios: MutableList<String>)