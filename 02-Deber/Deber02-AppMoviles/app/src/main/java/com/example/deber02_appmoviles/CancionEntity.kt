package com.example.deber02_appmoviles

import android.os.Parcel
import android.os.Parcelable

class CancionEntity (
    var id:Int,
    var nombre: String,
    var edad: Int,
    var altura: Double
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    override fun toString(): String {
        return nombre
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeInt(edad)
        parcel.writeDouble(altura)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CancionEntity> {
        override fun createFromParcel(parcel: Parcel): CancionEntity {
            return CancionEntity(parcel)
        }

        override fun newArray(size: Int): Array<CancionEntity?> {
            return arrayOfNulls(size)
        }
    }

}