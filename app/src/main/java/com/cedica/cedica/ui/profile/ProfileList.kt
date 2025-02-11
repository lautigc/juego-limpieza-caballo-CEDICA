package com.cedica.cedica.ui.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
        if (currentUser.id != GuestUser.id) {
            item {
                Column(
                    Modifier
                        .border(3.dp, MaterialTheme.colorScheme.primaryContainer)
                        .then(userItemModifier)
                ) {
                    Text("Usuario actual")
                    UserItem(
                        userItem = users.first { it.id == currentUser.id },
                        currentUser = currentUser,
                        cardColors = CardDefaults.cardColors().copy(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        onLogin = { onLogin(currentUser) },
                        onUserSetting = onUserSetting,
                    )
                }
            }
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
                UserInformation(userItem, modifier = Modifier
                    .align(Alignment.Top)
                    .weight(0.8f))
                Spacer(modifier = Modifier.weight(1f))
                ItemUserActions(
                    isCurrent = userItem.id == currentUser.id,
                    onLogin = onLogin,
                    onUserSetting = onUserSetting,
                    userItem = userItem
                )
            }
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
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = user.firstName + " " + user.lastName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
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
    val sheetState = rememberModalBottomSheetState()
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