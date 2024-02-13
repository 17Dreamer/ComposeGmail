package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.dreamtech.compose.gmail.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTopBar(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
    onAccountClicked: () -> Unit = {}
) {

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp), horizontalArrangement = Arrangement.Center
    ) {
        val padding = if (active) 0.dp else 15.dp
        val shape = if (active) RectangleShape else SearchBarDefaults.dockedShape
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = padding),
            query = query,
            onQueryChange = {
                query = it
            },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text(text = stringResource(id = R.string.search_emails)) },
            leadingIcon = {
                if (active) {
                    IconButton(
                        onClick = {
                            active = false
                            query = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button),
                        )
                    }
                } else {

                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.search),
                        )
                    }
                }
            },
            trailingIcon = {
                if (active) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Mic, contentDescription = "")
                    }

                } else {
                    IconButton(onClick = onAccountClicked) {
                        ProfileImage(
                            drawableResource = R.drawable.account_avatar,
                            description = stringResource(id = R.string.profile),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            },
            shape = shape,
        ) {
        }
    }

}

@Composable
fun ProfileImage(
    drawableResource: Int,
    description: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        painter = painterResource(id = drawableResource),
        contentDescription = description,
        contentScale = contentScale
    )
}

@Composable
fun DefaultProfileImage(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Box(modifier, contentAlignment = Alignment.Center) {

        val initials = (firstName.take(1) + lastName.take(1)).uppercase()
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(Color(255, 193, 7, 255)))
        }
        Text(text = initials, style = textStyle, color = Color.White)
    }
}
