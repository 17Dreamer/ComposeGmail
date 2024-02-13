package com.dreamtech.compose.gmail.feature.threaddetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        onClick = onClick,
        modifier= modifier,
        contentPadding = PaddingValues(3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = icon, contentDescription = "", modifier = Modifier.padding(end = 3.dp), tint = MaterialTheme.colorScheme.outline)
            Text(text = text, color = MaterialTheme.colorScheme.outline,textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ActionButtonPreview(){
    ActionButton(icon = rememberVectorPainter(image = Icons.Default.FavoriteBorder), text = "Action")
}