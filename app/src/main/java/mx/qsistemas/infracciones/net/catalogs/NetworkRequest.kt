package mx.qsistemas.infracciones.net.catalogs

import com.google.gson.annotations.SerializedName

data class SaveInfractionRequest(@SerializedName("Infracciones") val infractions: List<InfractionRequest>,
                                 @SerializedName("password") val password: String,
                                 @SerializedName("username") val username: String) {

    data class InfractionRequest(
            @SerializedName("InfraccionBanInfractorAusente") val InfraccionBanInfractorAusente: Int,
            @SerializedName("InfraccionBanPagada") val InfraccionBanPagada: Int,
            @SerializedName("InfraccionBanRemitido") val InfraccionBanRemitido: Int,
            @SerializedName("InfraccionDocumentoRetenido") val InfraccionDocumentoRetenido: String,
            @SerializedName("InfraccionDomicilioCalle") val InfraccionDomicilioCalle: String,
            @SerializedName("InfraccionDomicilioColonia") val InfraccionDomicilioColonia: String,
            @SerializedName("InfraccionDomicilioEntreCalle") val InfraccionDomicilioEntreCalle: String,
            @SerializedName("InfraccionDomicilioIdEstado") val InfraccionDomicilioIdEstado: String,
            @SerializedName("InfraccionDomicilioIdMunicipio") val InfraccionDomicilioIdMunicipio: String,
            @SerializedName("InfraccionDomicilioIdPais") val InfraccionDomicilioIdPais: String,
            @SerializedName("InfraccionDomicilioX") val InfraccionDomicilioX: Int,
            @SerializedName("InfraccionDomicilioY") val InfraccionDomicilioY: Int,
            @SerializedName("InfraccionDomicilioYCalle") val InfraccionDomicilioYCalle: String,
            @SerializedName("InfraccionFecha") val InfraccionFecha: String,
            @SerializedName("InfraccionFechaLineaCapturaI") val InfraccionFechaLineaCapturaI: String,
            @SerializedName("InfraccionFechaLineaCapturaII") val InfraccionFechaLineaCapturaII: String,
            @SerializedName("InfraccionFechaLineaCapturaIII") val InfraccionFechaLineaCapturaIII: String,
            @SerializedName("InfraccionFolio") val InfraccionFolio: String,
            @SerializedName("InfraccionFolioEvidencia") val InfraccionFolioEvidencia: String,
            @SerializedName("InfraccionFracciones") val InfraccionFracciones: List<InfractionFractions_Request>,
            @SerializedName("InfraccionGuardar") val InfraccionGuardar: Int,
            @SerializedName("InfraccionIdAutoridadExpidePlaca") val InfraccionIdAutoridadExpidePlaca: Int,
            @SerializedName("InfraccionIdDisposicion") val InfraccionIdDisposicion: Int,
            @SerializedName("InfraccionIdEstatus") val InfraccionIdEstatus: Int,
            @SerializedName("InfraccionIdFuente") val InfraccionIdFuente: Int,
            @SerializedName("InfraccionIdOficial") val InfraccionIdOficial: Int,
            @SerializedName("InfraccionIdSector") val InfraccionIdSector: Int,
            @SerializedName("InfraccionImporteLineaCapturaI") val InfraccionImporteLineaCapturaI: String,
            @SerializedName("InfraccionImporteLineaCapturaII") val InfraccionImporteLineaCapturaII: String,
            @SerializedName("InfraccionImporteLineaCapturaIII") val InfraccionImporteLineaCapturaIII: String,
            @SerializedName("InfraccionImporteTotal") val InfraccionImporteTotal: Double,
            @SerializedName("InfraccionLineaCapturaI") val InfraccionLineaCapturaI: String,
            @SerializedName("InfraccionLineaCapturaII") val InfraccionLineaCapturaII: String,
            @SerializedName("InfraccionLineaCapturaIII") val InfraccionLineaCapturaIII: String,
            @SerializedName("InfraccionMotivo") val InfraccionMotivo: String,
            @SerializedName("InfraccionNumHojasExpediente") val InfraccionNumHojasExpediente: Int,
            @SerializedName("InfraccionPuntosSancion") val InfraccionPuntosSancion: Int,
            @SerializedName("InfraccionSalariosMinimosTotales") val InfraccionSalariosMinimosTotales: Int,
            @SerializedName("InfraccionTazaSalarioMinimo") val InfraccionTazaSalarioMinimo: Double,
            @SerializedName("InfractorDomicilioCalle") val InfractorDomicilioCalle: String,
            @SerializedName("InfractorDomicilioColonia") val InfractorDomicilioColonia: String,
            @SerializedName("InfractorDomicilioExiste") val InfractorDomicilioExiste: Boolean,
            @SerializedName("InfractorDomicilioIdEstado") val InfractorDomicilioIdEstado: Int,
            @SerializedName("InfractorDomicilioIdMunicipioSEPOMEX") val InfractorDomicilioIdMunicipioSEPOMEX: Int,
            @SerializedName("InfractorDomicilioIdPais") val InfractorDomicilioIdPais: String,
            @SerializedName("InfractorDomicilioMunicipioSEPOMEX") val InfractorDomicilioMunicipioSEPOMEX: String,
            @SerializedName("InfractorDomicilioNumeroExterior") val InfractorDomicilioNumeroExterior: String,
            @SerializedName("InfractorDomicilioNumeroInterior") val InfractorDomicilioNumeroInterior: String,
            @SerializedName("InfractorIdTipoLicencia") val InfractorIdTipoLicencia: Int,
            @SerializedName("InfractorLicenciaExpedidaEn") val InfractorLicenciaExpedidaEn: Int,
            @SerializedName("InfractorMaterno") val InfractorMaterno: String,
            @SerializedName("InfractorNombre") val InfractorNombre: String,
            @SerializedName("InfractorNumPermisoLicencia") val InfractorNumPermisoLicencia: String,
            @SerializedName("InfractorPaterno") val InfractorPaterno: String,
            @SerializedName("InfractorRfc") val InfractorRfc: String,
            @SerializedName("PagoInfraccionDescuento") val PagoInfraccionDescuento: Float,
            @SerializedName("PagoInfraccionIdFormaPago") val PagoInfraccionIdFormaPago: Int,
            @SerializedName("PagoInfraccionObservacion") val PagoInfraccionObservacion: String,
            @SerializedName("PagoInfraccionRecargos") val PagoInfraccionRecargos: Float,
            @SerializedName("PagoInfraccionSubtotal") val PagoInfraccionSubtotal: Float,
            @SerializedName("PagoInfraccionTotal") val PagoInfraccionTotal: Float,
            @SerializedName("PagoTarjetaAID") val PagoTarjetaAID: String,
            @SerializedName("PagoTarjetaAPPLabel") val PagoTarjetaAPPLabel: String,
            @SerializedName("PagoTarjetaARQC") val PagoTarjetaARQC: String,
            @SerializedName("PagoTarjetaAfiliacion") val PagoTarjetaAfiliacion: String,
            @SerializedName("PagoTarjetaBancoEmisor") val PagoTarjetaBancoEmisor: String,
            @SerializedName("PagoTarjetaEMV_Data") val PagoTarjetaEMV_Data: String,
            @SerializedName("PagoTarjetaEntryType") val PagoTarjetaEntryType: String,
            @SerializedName("PagoTarjetaImporte") val PagoTarjetaImporte: String,
            @SerializedName("PagoTarjetaMaskedPAN") val PagoTarjetaMaskedPAN: String,
            @SerializedName("PagoTarjetaMensaje") val PagoTarjetaMensaje: String,
            @SerializedName("PagoTarjetaNumeroAutorizacion") val PagoTarjetaNumeroAutorizacion: String,
            @SerializedName("PagoTarjetaNumeroControl") val PagoTarjetaNumeroControl: String,
            @SerializedName("PagoTarjetaReferencia") val PagoTarjetaReferencia: String,
            @SerializedName("PagoTarjetaSerieMovil") val PagoTarjetaSerieMovil: String,
            @SerializedName("PagoTarjetaTSI") val PagoTarjetaTSI: String,
            @SerializedName("PagoTarjetaTVR") val PagoTarjetaTVR: String,
            @SerializedName("PagoTarjetaTarjetaHabiente") val PagoTarjetaTarjetaHabiente: String,
            @SerializedName("PagoTarjetaTipo") val PagoTarjetaTipo: String,
            @SerializedName("PagoTarjetaTipoTarjeta") val PagoTarjetaTipoTarjeta: String,
            @SerializedName("PagoTarjetaTipoTransaccion") val PagoTarjetaTipoTransaccion: String,
            @SerializedName("PagoTarjetaTrxDate") val PagoTarjetaTrxDate: String,
            @SerializedName("PagoTarjetaTrxNb") val PagoTarjetaTrxNb: String,
            @SerializedName("PagoTarjetaTrxTime") val PagoTarjetaTrxTime: String,
            @SerializedName("PagoTarjetaVigenciaTarjeta") val PagoTarjetaVigenciaTarjeta: String,
            @SerializedName("SincronizarIdUsuario") val SincronizarIdUsuario: String,
            @SerializedName("VehiculoBarCodePlaca") val VehiculoBarCodePlaca: String,
            @SerializedName("VehiculoColor") val VehiculoColor: String,
            @SerializedName("VehiculoIdDocumentoIdentificador") val VehiculoIdDocumentoIdentificador: Int,
            @SerializedName("VehiculoIdMarca") val VehiculoIdMarca: Int,
            @SerializedName("VehiculoModelo") val VehiculoModelo: String,
            @SerializedName("VehiculoNumeroDocumentoIdentificador") val VehiculoNumeroDocumentoIdentificador: String,
            @SerializedName("VehiculoSubMarca") val VehiculoSubMarca: String,
            @SerializedName("VehiculoTarjetaCirculacion") val VehiculoTarjetaCirculacion: String,
            @SerializedName("VehiculoTipo") val VehiculoTipo: String) {
        data class InfractionFractions_Request(
                @SerializedName("FraccionIdFraccion") val FraccionIdFraccion: Int,
                @SerializedName("FraccionPuntosSancion") val FraccionPuntosSancion: Int,
                @SerializedName("FraccionSalariosMinimos") val FraccionSalariosMinimos: Int,
                @SerializedName("FraccionMotivacion") val FraccionMotivacion: String
        )
    }
}

data class InfractionPhotoRequest(@SerializedName("LoteImagenes") val images: List<PhotoData>,
                                  @SerializedName("password") val password: String,
                                  @SerializedName("username") val username: String) {

    data class PhotoData(@SerializedName("Tipo") val type: Int,
                         @SerializedName("ArchivoNombre") val fileName: String,
                         @SerializedName("ArchivoBase64") val file64: String)
}