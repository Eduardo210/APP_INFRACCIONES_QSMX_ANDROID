package mx.qsistemas.payments_transfer.dtos

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @param idc - Identificador del mensaje de cobro generado conforme al Anex C "Generación de identificadores para los mensajes de cobro presenciales"
 * @param des - Motivo por el que se instruye el cobro al comprador
 * @param amo - Dato numérico que indica el monto de cobro.  En caso de que se desee generar un mensaje sin monto, no se debe agregar este campo, en cuyo
 *              caso la App Bancaria del comprador y ordenante deberá solicitar esta información al usuario.
 * @param com - Valor que indica quien paga la comisión por la transferencia:
 *              1 - El cliente emisor de la transferencia paga.
 *              2 - El cliente beneficiario de la transferencia paga.
 * @param dat - Estampa de tiempo del momento de la generación de la solicitud, en milisegundos desde las 00:00:00.000 horas del 01 enero de 1970 en UTC
 * @param typ - Este campo indica el tipo de pago a emplear cuando la transferencia de fondos sea interbancaria:
 *              19 - Caso Presencial - Cobro de una sola ocasión
 *              20 - Caso No Presencial - Cobro de una sola ocasión
 *              21 - Cobro recurrentes
 *              22 - Cobros recurrentes y no recurrentes a nombre de un tercero
 * @param v   - [CoDi_Decypher]
 */
class CoDi_Decypher(
        @SerializedName("IDC") val idc: String,
        @SerializedName("DES") val des: String,
        @SerializedName("AMO") var amo: Double,
        @SerializedName("COM") val com: Int,
        @SerializedName("DAT") val dat: Long,
        @SerializedName("TYP") val typ: Int,
        @SerializedName("v") val v: V_Decypher) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readParcelable(V_Decypher::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idc)
        parcel.writeString(des)
        parcel.writeDouble(amo)
        parcel.writeInt(com)
        parcel.writeLong(dat)
        parcel.writeInt(typ)
        parcel.writeParcelable(v, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoDi_Decypher> {
        override fun createFromParcel(parcel: Parcel): CoDi_Decypher {
            return CoDi_Decypher(parcel)
        }

        override fun newArray(size: Int): Array<CoDi_Decypher?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * @param nam - Nombre del vendedor o beneficiario configurado en la App CoDi o App Bancaria
 * @param acc - Cuenta del vendedor configurada en la app generadora de cobros para la recepción de pagos
 * @param ban - Clave SPEI de la institución bancaria del vendedor (receptor del pago)
 * @param tyc - Tipo de cuenta del vendedor configurada en la app generadora de cobros para la recepción de pagos
 * @param dev - Número de celular del vendedor configurado en la App CoDi o App Bancaria concatenado con el carácter
 *              “/” y el Dígito verificador asignado por Banco de México en el registro del dispositivo
 */
class V_Decypher(@SerializedName("NAM") val nam: String, @SerializedName("ACC") val acc: String,
                 @SerializedName("BAN") val ban: Long, @SerializedName("TYC") val tyc: Int,
                 @SerializedName("DEV") val dev: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nam)
        parcel.writeString(acc)
        parcel.writeLong(ban)
        parcel.writeInt(tyc)
        parcel.writeString(dev)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<V_Decypher> {
        override fun createFromParcel(parcel: Parcel): V_Decypher {
            return V_Decypher(parcel)
        }

        override fun newArray(size: Int): Array<V_Decypher?> {
            return arrayOfNulls(size)
        }
    }
}