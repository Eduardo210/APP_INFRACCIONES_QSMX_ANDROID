package mx.qsistemas.payments_transfer.dtos

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Notification(@SerializedName("title") val title: String,
                   @SerializedName("body") val body: String,
                   @SerializedName("info") val info: Notif_Info?, //presencial
                   @SerializedName("payreq") val payreq: Pay_Req?, //no presencial
                   @SerializedName("infoCuenta") val infoCuenta: Info_Cuenta? //validación cuenta
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readParcelable(Notif_Info::class.java.classLoader),
            parcel.readParcelable(Pay_Req::class.java.classLoader),
            parcel.readParcelable(Info_Cuenta::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeParcelable(info, flags)
        parcel.writeParcelable(payreq, flags)
        parcel.writeParcelable(infoCuenta, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object CREATOR : Parcelable.Creator<Notification> {
        override fun createFromParcel(parcel: Parcel): Notification {
            return Notification(parcel)
        }

        override fun newArray(size: Int): Array<Notification?> {
            return arrayOfNulls(size)
        }
    }


}

/**
 * Notificación de estado de cobro presencial
 */
class Notif_Info(
        @SerializedName("infoCif") val infoCif: Notif_Info_Cif,
        @SerializedName("xx") val xx: Int //Si se quita causa error overload_resolution_ambiguity
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Notif_Info_Cif::class.java.classLoader)!!,
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(infoCif, flags)
        parcel.writeInt(xx)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notif_Info> {
        override fun createFromParcel(parcel: Parcel): Notif_Info {
            return Notif_Info(parcel)
        }

        override fun newArray(size: Int): Array<Notif_Info?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Notif_Info_Cif(
        @SerializedName("id") val id: String,
        @SerializedName("mc") val mc: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(mc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notif_Info_Cif> {
        override fun createFromParcel(parcel: Parcel): Notif_Info_Cif {
            return Notif_Info_Cif(parcel)
        }

        override fun newArray(size: Int): Array<Notif_Info_Cif?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Notif_Info_Dec(@SerializedName("c") val comprador: Comprador_Vendedor_Data,
                     @SerializedName("v") val vendedor: Comprador_Vendedor_Data,
                     @SerializedName("e") val estadoOperacion: Int,
                     @SerializedName("mt") val monto: Double,
                     @SerializedName("hp") val hp: Long,
                     @SerializedName("id") val id: String,
                     @SerializedName("hs") val hs: Long,
                     @SerializedName("cr") val claveRastreo: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Comprador_Vendedor_Data::class.java.classLoader)!!,
            parcel.readParcelable(Comprador_Vendedor_Data::class.java.classLoader)!!,
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readLong(),
            parcel.readString().toString(),
            parcel.readLong(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(comprador, flags)
        parcel.writeParcelable(vendedor, flags)
        parcel.writeInt(estadoOperacion)
        parcel.writeDouble(monto)
        parcel.writeLong(hp)
        parcel.writeString(id)
        parcel.writeLong(hs)
        parcel.writeString(claveRastreo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notif_Info_Dec> {
        override fun createFromParcel(parcel: Parcel): Notif_Info_Dec {
            return Notif_Info_Dec(parcel)
        }

        override fun newArray(size: Int): Array<Notif_Info_Dec?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Pay_Req(
        @SerializedName("infoCif") val infoCif: Mensaje_Cobro_Cifrado_Data,
        @SerializedName("x") val x: Int?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Mensaje_Cobro_Cifrado_Data::class.java.classLoader)!!,
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(infoCif, flags)
        parcel.writeValue(x)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pay_Req> {
        override fun createFromParcel(parcel: Parcel): Pay_Req {
            return Pay_Req(parcel)
        }

        override fun newArray(size: Int): Array<Pay_Req?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class Info_Cuenta(
        @SerializedName("cr") val cr: String,
        @SerializedName("infCif") val infCif: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cr)
        parcel.writeString(infCif)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Info_Cuenta> {
        override fun createFromParcel(parcel: Parcel): Info_Cuenta {
            return Info_Cuenta(parcel)
        }

        override fun newArray(size: Int): Array<Info_Cuenta?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}