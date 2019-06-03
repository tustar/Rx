package com.tustar.filemanager.ui.detail

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.tustar.filemanager.annotation.CategoryType

data class DetailParams(
        @CategoryType val type: Int,
        val directoryUri: Uri? = null,
        val volumeName: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readParcelable(Uri::class.java.classLoader),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeParcelable(directoryUri, flags)
        parcel.writeString(volumeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailParams> {
        override fun createFromParcel(parcel: Parcel): DetailParams {
            return DetailParams(parcel)
        }

        override fun newArray(size: Int): Array<DetailParams?> {
            return arrayOfNulls(size)
        }
    }
}