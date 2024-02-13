package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import com.dreamtech.compose.gmail.utils.format

@Composable
fun EmailMessageCard(thread: EmailThread, modifier: Modifier = Modifier){
    Row(modifier = modifier.padding(vertical = 10.dp)) {
        //if (thread.avatar != null) {
//            ProfileImage(
//                drawableResource = email.avatar, description = "",
//                modifier = Modifier
//                    .size(70.dp)
//                    .padding(15.dp),
//                contentScale = ContentScale.Crop
//            )
        //} else {
            DefaultProfileImage(
                firstName = thread.title, lastName = "", modifier = Modifier
                    .size(70.dp)
                    .padding(15.dp)
            )
        //}
        Column (
            Modifier
                .fillMaxWidth()
                .weight(1f)){
            Text(text = thread.title, fontSize = 16.sp, maxLines = 1)
            Text(text = thread.subject, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = thread.snippet, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 10.dp)) {
            Text(text = thread.date.format(), fontSize = 12.sp)
            Icon(painter = painterResource(id = R.drawable.ic_navigation_starred), contentDescription = "", modifier = Modifier.padding(10.dp))
        }
    }

}