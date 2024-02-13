package com.dreamtech.compose.gmail.feature.mainscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.feature.mainscreen.NavigationItem
import com.dreamtech.compose.gmail.feature.mainscreen.NavigationUiState

@Composable
fun EmailDrawerContent(
    uiState : NavigationUiState,
    onDrawerClicked: (NavigationItem) -> Unit = {}
) {
    ModalDrawerSheet(
        drawerShape = RectangleShape,
        drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            content = {

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start,
                ) {
                    val navigationItems = uiState.items
                    val navigationLabels = uiState.labels
                    val recentLabels = uiState.recentLabels
                    val selectedItem = uiState.selected

                    Text(
                        stringResource(id = R.string.app_name),
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
                        fontSize = 18.sp,
                        color = Color(0xFFEA4335)
                    )
                    DrawerDivider()
                    if(uiState.showAll) {
                        val allBoxes = NavigationItem(
                            labelRes = R.string.navigation_all_boxes,
                            iconRes = R.drawable.ic_navigation_all_boxes
                        )
                        NavigationItem(selectedItem == allBoxes, allBoxes, onDrawerClicked)
                        DrawerDivider()
                    }

                    navigationItems.forEach { item ->
                        NavigationItem(selectedItem == item, item, onDrawerClicked)
                    }

                    if(recentLabels.isNotEmpty()) {
                        NavigationSection(stringResource(id = R.string.navigation_category_recent_labels))

                        recentLabels.forEach { item ->
                            NavigationItem(selectedItem == item, item, onDrawerClicked)
                        }
                    }

                    NavigationSection(stringResource(id = R.string.navigation_category_all_labels))

                    navigationLabels.forEach { item ->
                        NavigationItem(selectedItem == item, item, onDrawerClicked)
                    }

                    NavigationSection(stringResource(id = R.string.navigation_category_google_apps))
                    NavigationItem(
                        selected = false,
                        item = NavigationItem(
                            labelRes = R.string.navigation_calendar,
                            iconRes = R.drawable.ic_navigation_calendar
                        ),
                        onDrawerClicked = onDrawerClicked
                    )
                    NavigationItem(
                        selected = false,
                        item = NavigationItem(
                            labelRes = R.string.navigation_contacts,
                            iconRes = R.drawable.ic_navigation_contact
                        ),
                        onDrawerClicked = onDrawerClicked
                    )

                    DrawerDivider()

                    NavigationItem(
                        selected = false,
                        item = NavigationItem(
                            labelRes = R.string.navigation_settings,
                            iconRes = R.drawable.ic_navigation_settings
                        ),
                        onDrawerClicked = onDrawerClicked
                    )
                    NavigationItem(
                        selected = false,
                        item = NavigationItem(
                            labelRes = R.string.navigation_help_feedback,
                            iconRes = R.drawable.ic_navigation_help
                        ),
                        onDrawerClicked = onDrawerClicked
                    )
                }

            },
            measurePolicy = { measurables, constraints ->
                lateinit var contentMeasurable: Measurable
                measurables.forEach {
                    when (it.layoutId) {
                        LayoutType.CONTENT -> contentMeasurable = it
                        else -> error("Unknown layoutId encountered!")
                    }
                }

                val contentPlaceable = contentMeasurable.measure(
                    constraints
                )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    contentPlaceable.placeRelative(0, 0)
                }
            }
        )
    }
}

@Composable
private fun NavigationSection(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 15.dp)
    )
}

@Composable
private fun NavigationItem(
    selected: Boolean,
    item: NavigationItem,
    onDrawerClicked: (NavigationItem) -> Unit
) {
    NavigationDrawerItem(
        modifier = Modifier
            .height(50.dp)
            .padding(end = 8.dp),
        shape = RoundedCornerShape(0.dp, 100.dp, 100.dp, 0.dp),
        selected = selected,
        label = {
            Text(
                text = item.label?:stringResource(id = item.labelRes),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.label?:stringResource(id = item.labelRes),
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
            )
        },
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
        onClick = { onDrawerClicked(item) },
        badge = {
            if (item.badger > 0) {
                Text(
                    text = item.badger.toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    )
}

@Composable
fun DrawerDivider() {
    Divider(Modifier.padding(vertical = 7.dp))
}

enum class LayoutType {
    HEADER, CONTENT
}
