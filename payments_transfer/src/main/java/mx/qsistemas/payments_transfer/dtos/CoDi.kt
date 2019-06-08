package mx.qsistemas.payments_transfer.dtos

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @param typ - Este campo indica el tipo de pago a emplear cuando la transferencia de fondos sea interbancaria:
 *              19 - Caso presencial Cobros de una ocasión
 *              20 - Casos no presenciales Cobros de una ocasión.
 *              21 = Cobros recurrentes.
 *              22 = Cobros recurrentes y no recurrentes a nombre de un tercero.
 * @param v - [V]
 * @param ic - [IC]
 * @param cry - Cadena de validación de integridad del mensaje de cobro (HMAC)
 */
class CoDi(
        @SerializedName("TYP") val typ: Int,
        @SerializedName("v") val v: V,
        @SerializedName("ic") val ic: IC,
        @SerializedName("CRY") val cry: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readParcelable(V::class.java.classLoader)!!,
            parcel.readParcelable(IC::class.java.classLoader)!!,
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(typ)
        parcel.writeParcelable(v, flags)
        parcel.writeParcelable(ic, flags)
        parcel.writeString(cry)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoDi> {
        override fun createFromParcel(parcel: Parcel): CoDi {
            return CoDi(parcel)
        }

        override fun newArray(size: Int): Array<CoDi?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * @param dev - Número de celular del vendedor configurado en la App CoDi o App Bancaria concatenado con el carácter
 *              “/” y el Dígito verificador asignado por Banco de México en el registro del dispositivo
 */
class V(@SerializedName("DEV") val dev: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dev)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<V> {
        override fun createFromParcel(parcel: Parcel): V {
            return V(parcel)
        }

        override fun newArray(size: Int): Array<V?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * @param idc - Identificador del mensaje de cobro generado conforme al Anexo C “Generación de identificadores
 *              para los mensajes de cobro presenciales"
 * @param ser - Número de serie único de cobro del participante. Se recomienda generar este serial a partir del
 *              número 1 y reiniciarlo cada cierto tiempo, pudiendo ser diario
 * @param enc - Cadena del mensaje de cobro cifrad
 */
class IC(@SerializedName("IDC") val idc: String,
         @SerializedName("SER") val ser: Int,
         @SerializedName("ENC") val enc: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readInt(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idc)
        parcel.writeInt(ser)
        parcel.writeString(enc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IC> {
        override fun createFromParcel(parcel: Parcel): IC {
            return IC(parcel)
        }

        override fun newArray(size: Int): Array<IC?> {
            return arrayOfNulls(size)
        }
    }
}