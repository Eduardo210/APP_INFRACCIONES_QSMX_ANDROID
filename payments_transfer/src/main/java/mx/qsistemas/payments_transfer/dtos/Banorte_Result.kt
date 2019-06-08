package mx.qsistemas.payments_transfer.dtos

import com.google.gson.annotations.SerializedName

open class Result_Generic {
    @SerializedName("CODIGO_PAYW")
    open var codigoPayW: String? = null

    @SerializedName("RESULTADO_PAYW")
    open var resultadoPayW: String? = null

    @SerializedName("FECHA_REQ_CTE")
    open var dateRequest: String? = null

    @SerializedName("FECHA_RSP_CTE")
    open var dateResponse: String? = null
}

data class Result_ProcessTx(@SerializedName("NUMERO_CONTROL") var noControl: String = "",
                            @SerializedName("REFERENCIA") var reference: String = "",
                            @SerializedName("FECHA_REQ_AUT") var fechaReqAut: String = "",
                            @SerializedName("FECHA_RSP_AUT") var fechaRespAut: String = "",
                            @SerializedName("RESULTADO_AUT") var resultadoAut: String = "",
                            @SerializedName("CODIGO_AUT") var codigoAut: String = "",
                            @SerializedName("TEXTO") var texto: String = "",
                            @SerializedName("BANCO_EMISOR") var bancoEmisor: String = "",
                            @SerializedName("MARCA_TARJETA") var marcaTarjeta: String = "",
                            @SerializedName("TIPO_TARJETA") var tipoTarjeta: String = "",
                            @SerializedName("ID_AFILIACION") var idAfiliacion: String = "",
                            @SerializedName("DATOS_EMV") var datosEmv: String = "",
                            @SerializedName("TARJETA_REFERIDA") var tarjetaReferida: String = "") : Result_Generic()