package com.example.deber02_appmoviles

import android.os.Parcel
import android.os.Parcelable

class ArtistaEntity (
    var id: Int,
    var nombre: String,
    var fechaCreacion: String,
    var ciudad: String,
    var canciones: MutableList<Int>?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readArrayList(Int::class.java.classLoader) as MutableList<Int>
    ) {}

    override fun toString(): String {
        return nombre
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(fechaCreacion)
        parcel.writeString(ciudad)
        parcel.writeList(canciones)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArtistaEntity> {
        override fun createFromParcel(parcel: Parcel): ArtistaEntity {
            return ArtistaEntity(parcel)
        }

        override fun newArray(size: Int): Array<ArtistaEntity?> {
            return arrayOfNulls(size)
        }
    }
}