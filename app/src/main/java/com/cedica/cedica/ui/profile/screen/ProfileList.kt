package com.cedica.cedica.ui.profile.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import com.cedica.cedica.data.permissions.Permission
import com.cedica.cedica.data.permissions.hasPermission
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.composables.BottomSheetMenu
import com.cedica.cedica.ui.utils.composables.BottomSheetMenuItem
import com.cedica.cedica.ui.utils.composables.SimpleAlertDialog

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
    containerColor: Color = MaterialTheme.colorScheme.inversePrimary,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    indicatorColor: Color = MaterialTheme.colorScheme.primary
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
                    Text(text = tabTitle, style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // Display the content screen corresponding to the selected tab
        contentScreens[selectedTabIndex].invoke()
    }
}

@Composable
fun UserList(
    users: List<User>,
    currentUser: User,
    onLogin: (User) -> Unit = {},
    onDelete: (User) -> Unit = {},
    onEdit: (userID: Long) -> Unit = {},
    onDetail: @Composable (id: Long) -> Unit = {},
    onUserSetting: ((Long) -> Unit)? = {},
    permission: PermissionData = PermissionData(),
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
            item {
                UserItem(
                    userItem = currentUser,
                    modifier = userItemModifier,
                    onLogin = { onLogin(currentUser) },
                    onDelete = { onDelete(currentUser) },
                    onEdit = { onEdit(currentUser.id) },
                    onDetail = @Composable { onDetail(currentUser.id) },
                    onUserSetting = onUserSetting?.let { { it(currentUser.id) } },
                    permission = permission,
                    selected = true,
                )
            }
        }

        itemsIndexed(users) { _, user ->
            if (user.id != currentUser.id) {
                UserItem(
                    userItem = user,
                    modifier = userItemModifier,
                    onLogin = { onLogin(user) },
                    onDelete = { onDelete(user) },
                    onDetail = @Composable { onDetail(user.id) },
                    onUserSetting = onUserSetting?.let { { it(currentUser.id) } },
                    onEdit = { onEdit(user.id) },
                    permission = permission,
                )
            }
        }
        item { Spacer(modifier = Modifier.size(200.dp)) }
    }
}

@Composable
fun SelectedContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        OutlinedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_medium))
        ) {
            content()
        }
    }
}

@Composable
fun SimpleContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier,
    ) {
        content()
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
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column {
            Text(
                text = user.username,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_extra_small))
            )
            Text(
                text = user.fullName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Composable
fun UserItem(
    userItem: User,
    onLogin: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onUserSetting: (() -> Unit)? = {},
    onDetail: @Composable () -> Unit = {},
    permission: PermissionData = PermissionData(),
    selected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val container: @Composable (Modifier, @Composable () -> Unit) -> Unit =
        if (selected) { mod, cont -> SelectedContainer(mod, cont) }
        else { mod, cont -> SimpleContainer(mod, cont) }

    container(modifier) {
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
                )

                ItemUserActions(
                    isCurrent = selected,
                    onLogin = onLogin,
                    onDelete = onDelete,
                    onDetail = onDetail,
                    onEdit = onEdit,
                    onUserSetting = onUserSetting,
                    userItem = userItem,
                    permission = permission
                )
            }
        }
    }
}

data class PermissionData(
    val onEdit: List<Permission> = emptyList(),
    val onDelete: List<Permission> = emptyList(),
    val onConfig: List<Permission> = emptyList(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemUserActions(
    isCurrent: Boolean = false,
    onLogin: () -> Unit = {},
    onDelete: () -> Unit = {},
    onUserSetting: (() -> Unit)? = {},
    onDetail: @Composable () -> Unit = {},
    onEdit: () -> Unit = {},
    permission: PermissionData = PermissionData(),
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
    ) { showBottomSheet ->
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

        var showDetail by rememberSaveable { mutableStateOf(false) }
        BottomSheetMenuItem(
            label = "Detalles",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                )
            },
            onClick = { showDetail = true },
            modifier = itemModifier
        )
        showDetail.takeIf { it }?.let {
            AlertDialog(
                icon = { Icon(Icons.Outlined.Info, contentDescription = "Example Icon") },
                title = {
                    Text(
                    text = "Detalles: ${userItem.fullName}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                ) },
                text = { onDetail() },
                onDismissRequest = { showDetail = false },
                confirmButton = {
                    TextButton(
                        onClick = { showDetail = false }
                    ) {
                        Text("Cerrar")
                    }
                },
                dismissButton = null
            )
        }

        if (hasPermission(permission.onEdit) || isCurrent) {
            BottomSheetMenuItem(
                label = "Editar",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                    )
                },
                onClick = onEdit,
                modifier = itemModifier
            )
        }

        if (onUserSetting != null && (hasPermission(permission.onConfig) || isCurrent)) {
            BottomSheetMenuItem(
                label = "Configuracion",
                leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                onClick = onUserSetting,
                modifier = itemModifier
            )
        }

        if (hasPermission(permission.onDelete) && isCurrent) {
            var onDeleteOpenDialog by rememberSaveable { mutableStateOf(false) }
            BottomSheetMenuItem(
                label = "Eliminar",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                    )
                },
                onClick = { onDeleteOpenDialog = true },
                modifier = itemModifier
            )
            onDeleteOpenDialog.takeIf { it }?.let {
                SimpleAlertDialog(
                    onDismissRequest = { onDeleteOpenDialog = false },
                    onConfirmation = {
                        onDeleteOpenDialog = false
                        onDelete()
                        showBottomSheet.value = false
                    },
                    dialogTitle = "Eliminar",
                    dialogText = "¿Estás seguro de que deseas eliminar este usuario?",
                    icon = Icons.Default.Delete
                )
            }
        }
    }

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
fun UserInformationPreview() {
    CedicaTheme {
        UserInformation(
            user = users_seed.first()
        )
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