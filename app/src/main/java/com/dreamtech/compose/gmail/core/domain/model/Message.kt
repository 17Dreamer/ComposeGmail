package com.dreamtech.compose.gmail.core.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.dreamtech.compose.gmail.core.data.model.MessageEntity

data class Message(
    val id: String,
    val thread: String,
    val from: String,
    val destination: String,
    val ccDestination: String,
    val bccDestination: String,
    val dateTime: Long,
    val content: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(thread)
        parcel.writeString(from)
        parcel.writeString(destination)
        parcel.writeString(ccDestination)
        parcel.writeString(bccDestination)
        parcel.writeLong(dateTime)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {

        fun from(entity: MessageEntity): Message {
            return Message(
                id = entity.messageId,
                thread = entity.thread,
                from = entity.sender,
                destination = entity.destination,
                ccDestination = entity.ccDestination,
                bccDestination = entity.bccDestination,
                dateTime = entity.dateTime,
                content = entity.content
            )
        }

        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}
