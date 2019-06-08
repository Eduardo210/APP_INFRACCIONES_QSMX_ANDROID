package mx.qsistemas.payments_transfer.dtos

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class RegistroInicial_Result(
        @SerializedName("gId") val gId: String,
        @SerializedName("dv") val dv: Int,
        @SerializedName("edoPet") val edoPet: Int)

class RegistroDispositivo_Result(@SerializedName("dv") val dv: Int, @SerializedName("dvOmision") val dvOmision: Int,
                                 @SerializedName("edoPet") val edoPet: Int) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class RegistroDispositivoPorOmision_Result(@SerializedName("edoPet") val edoPet: Int)

class BajaDispositivos_Result(@SerializedName("cadenaResultadosCif") val cadenaResultadosCif: String,
                              @SerializedName("edoPet") val edoPet: Int, @SerializedName("serieCertBM") val serieCertBM: String,
                              @SerializedName("selloBM") val selloBM: String)

class Consulta_Result(
        @SerializedName("info") val info: Consulta_Data,
        @SerializedName("edoPet") val edoPet: Int) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Consulta_Data(
        @SerializedName("listaMC") val listaMC: String,
        @SerializedName("ultima") val ultima: Boolean) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Mensaje_Cobro_Decipher(@SerializedName("id") val id: String, @SerializedName("cc") val cc: String,
                             @SerializedName("mt") val mt: Double, @SerializedName("cr") val cr: String,
                             @SerializedName("hs") val hs: Long, @SerializedName("hp") val hp: Long,
                             @SerializedName("e") val e: Long, @SerializedName("c") val c: Comprador_Vendedor_Data,
                             @SerializedName("v") val v: Comprador_Vendedor_Data) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Comprador_Vendedor_Data(
        @SerializedName("nb") val nb: String,
        @SerializedName("ci") val ci: Long,
        @SerializedName("tc") val tc: Long,
        @SerializedName("cb") val cb: String,
        @SerializedName("dv") val dv: Long,
        @SerializedName("nc") val nc: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString().toString(),
            parcel.readLong(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nb)
        parcel.writeLong(ci)
        parcel.writeLong(tc)
        parcel.writeString(cb)
        parcel.writeLong(dv)
        parcel.writeString(nc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comprador_Vendedor_Data> {
        override fun createFromParcel(parcel: Parcel): Comprador_Vendedor_Data {
            return Comprador_Vendedor_Data(parcel)
        }

        override fun newArray(size: Int): Array<Comprador_Vendedor_Data?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class SolicitudClaveDescifrado_Result(@SerializedName("claveEnmascCr") val claveEnmascarada: String,
                                      @SerializedName("serieCertBmx") val serieCertificado: String,
                                      @SerializedName("selloBmx") val selloBmx: String,
                                      @SerializedName("edoPet") val edoPet: Int)

class ValidacionCuentasBeneficiarias_Result(
        @SerializedName("cr") val claveRastreo: String,
        @SerializedName("edoPet") val edoPet: Int)

class ConsultaValidacionCuentasBeneficiarias_Result(
        @SerializedName("infCif") val infCif: String, //Información Cifrada de la cuenta (Beneficiario_Ordenante_Data)
        @SerializedName("edoPet") val edoPet: Int)

class ValidacionCuentasDecryp_Data(
        @SerializedName("ds") val ds: Beneficiario_Ordenante_Data,
        @SerializedName("tc") val tc: Int,
        @SerializedName("cb") val cb: String,
        @SerializedName("ci") val ci: Int,
        // @SerializedName("nombreCDA") val nombreCDA: String,
        @SerializedName("hmac") val hmac: String,
        @SerializedName("cr") val cr: String,
        @SerializedName("rv") val rv: Int)

//todo implementar funcionalidad de consulta de cobros pendientes
class MovementsCodiResult(
        @SerializedName("info") val info: InfoMovements,
        @SerializedName("edoPet") val edoPet: Int
        /**
         * {
         *  "info": {
         *      "listaMC": String, //Información cifrada de las operaciones
         *      "ultima": Boolean //Indicador de que se entregó la última página
         *  },
         *  "edoPet": Numeric //Resultado de la consulta
         * }
         */


)

class InfoMovements(
        @SerializedName("listaMC") val listaMC: String,
        @SerializedName("ultima") val ultima: Boolean


) {

}

class MovementsCodiDecryptResult(


)