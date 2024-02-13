package com.dreamtech.compose.gmail.core.data.model

import androidx.compose.runtime.Composable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "label",
)
data class LabelEntity(
    @PrimaryKey(autoGenerate = true)
    val labelId: Int,
    val name: String,
    val account: String,
    val messageListVisibility: Boolean,
    val labelListVisibility: Boolean,
    val type: String,
    val messagesTotal: Int,
    val messagesUnread: Int,
    val threadsTotal: Int,
    val threadsUnread: Int,
){

    companion object{
        //Labels created by Gmail.
        const val TYPE_SYSTEM = "system"
        //Custom labels created by the user or application.
        const val TYPE_USER = "user"

        //Show the label in the message list.
        const val MESSAGE_LIST_VISIBILITY_SHOW = "show"
        //Do not show the label in the message list.
        const val MESSAGE_LIST_VISIBILITY_HIDE = "hide"

        //Show the label in the label list.
        const val LABEL_LIST_VISIBILITY_SHOW = "labelShow"
        //Show the label if there are any unread messages with that label.
        const val LABEL_LIST_VISIBILITY_SHOW_IF_UNREAD = "labelShowIfUnread"
        //Do not show the label in the label list.
        const val LABEL_LIST_VISIBILITY_SHOW_HIDE = "labelHide"

        enum class Type{
            System,
            User
        }
    }
}