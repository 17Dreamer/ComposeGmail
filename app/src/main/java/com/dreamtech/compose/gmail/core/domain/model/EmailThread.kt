package com.dreamtech.compose.gmail.core.domain.model

import com.dreamtech.compose.gmail.core.data.model.ThreadEntity


data class EmailThread(
    val id: String,
    val title: String,
    val subject: String,
    val snippet: String,
    val messageCount: Int,
    val date: Long,
    val isStarred: Boolean,
    val isLabeled: Boolean,
){
    companion object {
        fun from(threadEntity: ThreadEntity): EmailThread {
            return EmailThread(
                id = threadEntity.threadId,
                title = threadEntity.title,
                subject = threadEntity.subject,
                snippet = threadEntity.snippet,
                messageCount = threadEntity.messageCount,
                date = threadEntity.date,
                isStarred = threadEntity.isStarred,
                isLabeled = threadEntity.isLabeled,
            )
        }
    }
}
