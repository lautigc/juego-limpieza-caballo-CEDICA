package com.cedica.cedica.ui.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.theme.CedicaTheme

@Composable
fun UserList(
    users: List<User>,
    currentUser: User,
    onLogin: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    val userItemModifier = Modifier.padding(dimensionResource(R.dimen.padding_small))

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
    ) {
        if (currentUser.id != GuestUser.id) {
            item {
                Column(
                    Modifier.border(3.dp, MaterialTheme.colorScheme.primaryContainer)
                        .then(userItemModifier)
                ) {
                    Text("Usuario actual")
                    UserItem(
                        userItem = users.first { it.id == currentUser.id },
                        cardColors = CardDefaults.cardColors().copy(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        onLogin = { onLogin(currentUser) },
                        currentUser = currentUser,
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
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable  { mutableStateOf(false) }
    Card(
        modifier = modifier,
        colors = cardColors,
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
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
                UserInformation(userItem, modifier = Modifier.align(Alignment.Top).weight(0.8f))
                ExpandButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                )
            }
            if (expanded) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    ActionButtonGroup(
                        modifier = Modifier.padding(
                            top = 0.dp,
                            bottom = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_large),
                            start = dimensionResource(R.dimen.padding_large)
                        ),
                        isCurrent = userItem.id == currentUser.id,
                        onLogin = onLogin,
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionButtonGroup(
    modifier: Modifier = Modifier,
    isCurrent: Boolean = false,
    onLogin: () -> Unit,
) {
    val options = mutableListOf(
        Triple(Color.Cyan, Icons.Outlined.Info) {},
        Triple(Color.Yellow, Icons.Outlined.Edit) {},
        Triple(Color.Red, Icons.Outlined.Delete) {},
    )

    if (!isCurrent) {
        options.add(
            index = 0,
            element = Triple(
                first = Color.Green,
                second = Icons.AutoMirrored.Outlined.Login,
                third = { onLogin() }
            )
        )
    }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, triple ->
            SegmentedButton(
                onClick = triple.third,
                selected = false,
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                    baseShape = RoundedCornerShape(8.dp)
                ),
                colors = SegmentedButtonColors(
                    activeContainerColor = triple.first,
                    activeContentColor = triple.first,
                    activeBorderColor = triple.first,
                    inactiveContainerColor = triple.first,
                    inactiveContentColor = triple.first,
                    inactiveBorderColor = triple.first,
                    disabledActiveContainerColor = triple.first,
                    disabledActiveContentColor = triple.first,
                    disabledActiveBorderColor = triple.first,
                    disabledInactiveContainerColor = triple.first,
                    disabledInactiveContentColor = triple.first,
                    disabledInactiveBorderColor = triple.first
                ),
                label = {
                    Row {
                        Icon(imageVector = triple.second, contentDescription = null, tint = Color.Black)
                        Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
                    }
                },
            )
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
        UserList(users_seed, users_seed.first(), onLogin = {})
    }
}

/**
 * Composable that displays what the UI of the app looks like in dark theme in the design tab.
 */
@Preview(showBackground = true)
@Composable
fun UserListDarkPreview() {
    CedicaTheme(darkTheme = true) {
        UserList(users_seed, users_seed.first(), onLogin = {})
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    CedicaTheme {
        UserItem(users_seed.first(), currentUser = users_seed[2], onLogin = {})
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