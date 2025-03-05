package com.cedica.cedica.ui.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.theme.CedicaTheme
import kotlinx.coroutines.launch

/**
 * A custom Composable for creating a tabbed interface.
 *
 * @param tabs List of tab titles.
 * @param contentScreens List of Composable functions representing content screens for each tab.
 * @param modifier Modifier for the parent layout.
 * @param containerColor Background color for the tab row container.
 * @param contentColor Color for the text content of the tabs.
 * @param indicatorColor Color for the indicator line.
 */
@Composable
fun TabRowComponent(
    tabs: List<String>,
    contentScreens: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    indicatorColor: Color = MaterialTheme.colorScheme.inversePrimary
) {
    // State to keep track of the selected tab index
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    // Column layout to arrange tabs vertically and display content screens
    Column(modifier = modifier.fillMaxSize()) {
        // TabRow composable to display tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator = { tabPositions ->
                // Indicator for the selected tab
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = indicatorColor
                )
            }
        ) {
            // Iterate through each tab title and create a tab
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_medium)),
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                ) {
                    // Text displayed on the tab
                    Text(text = tabTitle)
                }
            }
        }

        // Display the content screen corresponding to the selected tab
        contentScreens[selectedTabIndex].invoke()
    }
}

@Composable
fun UserScreen(
    patients: List<User>,
    therapists: List<User>,
    currentUser: User,
    onLogin: (User) -> Unit = {},
    onUserSetting: () -> Unit = {},
) {
    TabRowComponent(
        tabs = listOf("Alumnos", "Maestros"),
        contentScreens = listOf(
            {
                UserList(
                    users = patients,
                    currentUser = currentUser,
                    onLogin = onLogin,
                    onUserSetting = onUserSetting,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            {
                UserList(
                    users = therapists,
                    currentUser = currentUser,
                    onLogin = onLogin,
                    onUserSetting = onUserSetting,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    )
}

@Composable
fun UserList(
    users: List<User>,
    currentUser: User,
    onLogin: (User) -> Unit = {},
    onUserSetting: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val userItemModifier = Modifier.padding(dimensionResource(R.dimen.padding_small))

    LazyColumn(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
    ) {
        if (currentUser in users) {
            CurrentUserItem(currentUser, userItemModifier, users, onLogin, onUserSetting)
        }

        itemsIndexed(users) { _, user ->
            if (user.id != currentUser.id) {
                UserItem(
                    userItem = user,
                    modifier = userItemModifier,
                    onLogin = { onLogin(user) },
                    onUserSetting = onUserSetting,
                    currentUser = currentUser,
                )
            }
        }
        item { Spacer(modifier = Modifier.size(200.dp)) }
    }
}


public fun LazyListScope.CurrentUserItem(
    currentUser: User,
    userItemModifier: Modifier,
    users: List<User>,
    onLogin: (User) -> Unit,
    onUserSetting: () -> Unit
) {


    if (currentUser.id != GuestUser.id) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                ),
                shape = MaterialTheme.shapes.medium,
            ) {
//                Text(
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(
//                        top = dimensionResource(R.dimen.padding_medium),
//                        start = dimensionResource(R.dimen.padding_medium),
//                        bottom = dimensionResource(R.dimen.padding_extra_small),
//                    ),
//                    text = "Usuario actual",
//                )
                OutlinedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_medium))
                ) {
                    ContentItem(
                        userItem = currentUser,
                        currentUser = currentUser,
                        onLogin = { onLogin(currentUser) },
                        onUserSetting = onUserSetting,
                    )
                }

            }
        }
    }
}

@Composable
fun UserItem(
    userItem: User,
    currentUser: User,
    cardColors: CardColors = CardDefaults.cardColors(),
    onLogin: () -> Unit,
    onUserSetting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = cardColors,
    ) {
        ContentItem(userItem, currentUser, onLogin, onUserSetting)
    }
}

@Composable
private fun ContentItem(
    userItem: User,
    currentUser: User,
    onLogin: () -> Unit,
    onUserSetting: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            UserIcon(
                rememberVectorPainter(Icons.Filled.Person),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
            UserInformation(
                userItem,
                modifier = Modifier
                    .align(Alignment.Top)
                    .weight(0.8f),
                isSelected = userItem.id == currentUser.id
            )
            //Spacer(modifier = Modifier.weight(1f))
            ItemUserActions(
                isCurrent = userItem.id == currentUser.id,
                onLogin = onLogin,
                onUserSetting = onUserSetting,
                userItem = userItem
            )
        }
    }
}

@Composable
private fun ExpandButton(
    expanded: Boolean,
    onClick: () -> Unit,
    lessIcon: ImageVector = Icons.Filled.ExpandLess,
    moreIcon: ImageVector = Icons.Filled.MoreVert,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) lessIcon else moreIcon,
            contentDescription = null,
        )
    }
}

@Composable
fun UserIcon(
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(60.dp)
            .padding(dimensionResource(R.dimen.padding_small))
            .border(2.dp, Color.Black, shape = MaterialTheme.shapes.small)
        ,
        contentScale = ContentScale.Crop,
        painter = icon,
        contentDescription = null
    )
}

@Composable
fun UserInformation(
    user: User,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = user.firstName + " " + user.lastName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                .weight(0.7f)
        )
        if (isSelected) {
            Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.CenterVertically)
                    .weight(0.3f)
            )
        }
    }
}

@Composable
fun ItemUserActions(
    isCurrent: Boolean = false,
    onLogin: () -> Unit = {},
    onUserSetting: () -> Unit = {},
    userItem: User,
    modifier: Modifier = Modifier,
) {
    val itemModifier = Modifier.padding(
        top = dimensionResource(R.dimen.padding_small),
        bottom = dimensionResource(R.dimen.padding_small)
    )

    BottomSheetMenu(
        expandElement = { Icon(Icons.Filled.MoreVert, contentDescription = null) },
        contentPaddingValues = PaddingValues(
            start = dimensionResource(R.dimen.padding_large),
            end  = dimensionResource(R.dimen.padding_large),
        ),
        modifier = modifier,
    ) {
        BottomSheetMenuItem(
            label = userItem.fullName,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .border(2.dp, Color.Black, shape = MaterialTheme.shapes.extraSmall)
                        .padding(all = dimensionResource(R.dimen.padding_extra_small))

                )
            },
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_large))
        )

        HorizontalDivider(modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large)))

        if(!isCurrent) {
            BottomSheetMenuItem(
                label = "Iniciar sesión",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Login,
                        contentDescription = null,
                    )
                },
                onClick = onLogin,
                modifier = itemModifier
            )
        }
        BottomSheetMenuItem(
            label = "Detalles",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                )
            },
            onClick = {},
            modifier = itemModifier
        )
        BottomSheetMenuItem(
            label = "Editar",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                )
            },
            onClick = {},
            modifier = itemModifier
        )
        BottomSheetMenuItem(
            label = "Configuracion",
            leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            onClick = onUserSetting,
            modifier = itemModifier
        )
        BottomSheetMenuItem(
            label = "Eliminar",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                )
            },
            onClick = {},
            modifier = itemModifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMenu(
    expandElement: @Composable (onExpandedMenu: () -> Unit) -> Unit = {},
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val expandButton = {
        scope.launch {
            sheetState.expand()
        }.invokeOnCompletion {
            showBottomSheet = true
        }
    }

    Box(
        modifier.clickable(
            onClick = { expandButton() },
        )
    ) {
        expandElement({ expandButton() })
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            sheetState = sheetState,
            modifier = modifier,
        ) {
            Column(modifier = Modifier
                .navigationBarsPadding()
                .padding(contentPaddingValues)
                .verticalScroll(rememberScrollState())
            ) {
                content()
            }
        }
    }
}

@Composable
fun BottomSheetMenuItem(
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        leadingIcon?.invoke()
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
        )
    }
    Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
}


/**
 * Composable that displays what the UI of the app looks like in light theme in the design tab.
 */
@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    CedicaTheme (darkTheme = false) {
        UserList(users_seed, users_seed.first(), onLogin = {}, { })
    }
}

/**
 * Composable that displays what the UI of the app looks like in dark theme in the design tab.
 */
@Preview(showBackground = true)
@Composable
fun UserListDarkPreview() {
    CedicaTheme(darkTheme = true) {
        UserList(users_seed, users_seed.first(), onLogin = {}, { })
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    CedicaTheme {
        UserItem(
            users_seed.first(),
            currentUser = users_seed[2],
            onLogin = {},
            onUserSetting = {  })
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemButtonPreview() {
    CedicaTheme {
        ExpandButton(
            expanded = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserIconPreview() {
    CedicaTheme {
        UserIcon(
            icon = rememberVectorPainter(Icons.Filled.Person)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInformationPreview() {
    CedicaTheme {
        UserInformation(
            user = users_seed.first()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DropdownMenuWithDetails() {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // First section
            DropdownMenuItem(
                text = { Text("Profile") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Second section
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                leadingIcon = { Icon(Icons.Outlined.Feedback, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Third section
            DropdownMenuItem(
                text = { Text("About") },
                leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Help") },
                leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabRowComponentPreview() {
    CedicaTheme {
        TabRowComponent(
            tabs = listOf("Tab 1", "Tab 2", "Tab 3"),
            contentScreens = listOf(
                { Text("Contenido de Tab 1") },
                { Text("Contenido de Tab 2") },
                { Text("Contenido de Tab 3") }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    CedicaTheme {
        UserScreen(
            patients = users_seed,
            therapists = users_seed.slice(3..6),
            currentUser = users_seed.first(),
            onLogin = {},
            onUserSetting = {}
        )
    }
}