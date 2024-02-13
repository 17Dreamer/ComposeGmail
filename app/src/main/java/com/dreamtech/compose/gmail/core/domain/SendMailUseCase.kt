package com.dreamtech.compose.gmail.core.domain

import com.dreamtech.compose.gmail.core.data.repository.ThreadsRepository
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendMailUseCase @Inject constructor(
    private val threadsRepository: ThreadsRepository,
) {
    suspend operator fun invoke(
        thread: String?,
        from: EmailAccount,
        destination: List<String> = emptyList(),
        cc: List<String> = emptyList(),
        bcc: List<String> = emptyList(),
        subject: String,
        content: String
    ): Result = withContext(Dispatchers.IO){
        val allDestinations = destination.plus(cc).plus(bcc)

        if (allDestinations.isEmpty()) {
            return@withContext Result.Error("At least one destination is required!")
        }

        val firstDestination = allDestinations[0]
        var title = firstDestination
        if (allDestinations.size == 2) {
            title = "$title, $allDestinations[1]"
        } else if (allDestinations.size > 2) {
            title = "$title, ${allDestinations.size}"
        }
        val snippet = content
        return@withContext threadsRepository.sendNewMail(
            thread = thread,
            account = from.address,
            title = title,
            destination = destination.joinToString(","),
            ccDestination  = cc.joinToString(","),
            bccDestination = bcc.joinToString(","),
            subject = subject,
            snippet = snippet,
            content = content
        )


    }
}