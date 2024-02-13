package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FilterSpinner(
    options: List<T>,
    item: @Composable (T, Modifier) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelectionChanged: (T) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }


    OutlinedCard(
        onClick = { expanded = true },
        modifier = modifier.padding(horizontal = 3.dp),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
    ) {
        Row(modifier = Modifier.padding(vertical = 3.dp, horizontal = 10.dp)) {
            Text(text = text)
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
        }
    }

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp),
            sheetState = sheetState
        ) {
            LazyColumn {
                items(options) {
                    item(it, Modifier.clickable {
                        onSelectionChanged(it)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                expanded = false
                            }
                        }

                    })
                }
            }
        }
    }

}