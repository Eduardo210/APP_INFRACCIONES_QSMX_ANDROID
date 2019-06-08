package mx.qsistemas.payments_transfer.dtos

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class RegistroInicial_Request(
        @SerializedName("nc") val phoneNumber: String,
        @SerializedName("idH") val idHardware: String,
        @SerializedName("ia") val additionalInfo: Aditional_Info_Data)

class RegistroDispositivo_Request(
        @SerializedName("nc") val phoneNumber: String,
        @SerializedName("idH") val idHardware: String,
        @SerializedName("ia") val additionalInfo: Aditional_Info_Data,
        @SerializedName("dv") val dv: Int,
        @SerializedName("idN") val idN: String,
        @SerializedName("hmac") val hmac: String)

class RegistroDispositivoPorOmision_Request(
        @SerializedName("nc") val phoneNumber: String,
        @SerializedName("dv") val dv: Int,
        @SerializedName("hmac") val hmac: String)

class BajaDispositivo_Request(
        @SerializedName("infoDispositivosCif") val infoDispositivosCif: String,
        @SerializedName("claveSimCif") val claveSimCif: String,
        @SerializedName("serieCertPartic") val serieCertPartic: String,
        @SerializedName("cvePartic") val cvePartic: Long,
        @SerializedName("selloPartic") val selloPartic: String,
        @SerializedName("serieCertBM") val serieCertBM: String)

class ConsultPresential_Request(
        @SerializedName("v") val v: Beneficiario_Ordenante_Data,
        @SerializedName("c") val c: Beneficiario_Ordenante_Data,
        @SerializedName("id") val id: String,
        @SerializedName("hmac") val hmac: String,
        @SerializedName("tpg") val tpg: Int,
        @SerializedName("npg") val npg: Int) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class SolicitudClaveDescifrado_Request(
        @SerializedName("tipo") val tipo: Int,
        @SerializedName("v") val v: Beneficiario_Ordenante_Data,
        @SerializedName("c") val c: Beneficiario_Ordenante_Data,
        @SerializedName("ic") val ic: Mensaje_Cobro_Cifrado_Data,
        @SerializedName("hmac") val hmac: String)


class ValidacionCuenta_Request(
        @SerializedName("cb") val cb: String,
        @SerializedName("tc") val tc: Int,
        @SerializedName("ci") val ci: Int,
        @SerializedName("hmac") val hmac: String,
        @SerializedName("ds") val ds: Beneficiario_Ordenante_Data)

class ConsultaValidacionCuenta_Request(
        @SerializedName("cr") val cr: String,
        @SerializedName("hmac") val hmac: String,
        @SerializedName("ds") val ds: Beneficiario_Ordenante_Data)


class GenMensajeCobroRequest(
        @SerializedName("cadenaInformacion") val cadenaInformacion: String,
        @SerializedName("selloDigital") val selloDigital: String //pag 453. Generado conforme al anexo B utilizando el certificado indicado en la cadena de información
)

class CadenaInformacionModel(
        val celularCliente: String, //len 10
        val referenciaNumerica: String, // len 7
        val concepto: String, //len 40
        val fechaLimitePago: String, //len 15
        val fechaSolicitud: String, //len 15
        val monto: String, //
        val nombreBeneficiario: String, //40
        val bancoBeneficiario: String,
        val tipoCuentaBeneficiario: String,
        val cuentaBeneficiario: String,
        val pagoComisionTransf: Int, //quien Paga la comision por la transferencia. El cliente emisor de la transferencia paga. 2. El cliente beneficiario de la transferencia paga
        val numserieCertificadoComercio: String, //Corresponde al número de serie del certificado utilizado para firmar la solicitud de cobro generada por el comercio o proveedor
        var nombreBeneficiario2: String, //solo 22
        var tipoCuentaBeneficiario2: String,//solo 22
        var cuentaBeneficiario2: String,//solo22
        val tipoPago: Int //20, 21, 22


) {
    override fun toString(): String {
        if (tipoPago != 22) {
            nombreBeneficiario2 = "NA"
            tipoCuentaBeneficiario2 = "NA"
            cuentaBeneficiario2 = "NA"
        }
        return "CadenaInformacionModel(celularCliente='$celularCliente', referenciaNumerica='$referenciaNumerica', concepto='$concepto', fechaLimitePago='$fechaLimitePago', fechaSolicitud='$fechaSolicitud', monto='$monto', nombreBeneficiario='$nombreBeneficiario', bancoBeneficiario='$bancoBeneficiario', tipoCuentaBeneficiario='$tipoCuentaBeneficiario', cuentaBeneficiario='$cuentaBeneficiario', pagoComisionTransf=$pagoComisionTransf, numserieCertificadoComercio='$numserieCertificadoComercio', nombreBeneficiario2='$nombreBeneficiario2', tipoCuentaBeneficiario2='$tipoCuentaBeneficiario2', cuentaBeneficiario2='$cuentaBeneficiario2', tipoPago=$tipoPago)"
    }
}

/**
 *  Clases Data : Objetos de 2do o mayor nivel en el Json
 */
class Aditional_Info_Data(
        @SerializedName("so") val so: String = "android",
        @SerializedName("vSO") val SO: String,
        @SerializedName("fab") val manufacturer: String,
        @SerializedName("mod") val model: String)

class Beneficiario_Ordenante_Data(
        @SerializedName("nc") val nc: String,
        @SerializedName("dv") val dv: Int)

class Mensaje_Cobro_Cifrado_Data(
        @SerializedName("id") val id: String,
        @SerializedName("s") val s: String,
        @SerializedName("mc") val mc: String)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(s)
        parcel.writeString(mc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mensaje_Cobro_Cifrado_Data> {
        override fun createFromParcel(parcel: Parcel): Mensaje_Cobro_Cifrado_Data {
            return Mensaje_Cobro_Cifrado_Data(parcel)
        }

        override fun newArray(size: Int): Array<Mensaje_Cobro_Cifrado_Data?> {
            return arrayOfNulls(size)
        }
    }
}