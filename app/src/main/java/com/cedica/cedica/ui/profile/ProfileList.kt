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
import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.theme.CedicaTheme

@Composable
fun UserList(
    users: List<User>,
    modifier: Modifier = Modifier
)
{
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
    ) {
        itemsIndexed(users) { _, user ->
            UserItem(
                user = user,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
        item { Spacer(modifier = Modifier.size(200.dp)) }
    }
}

@Composable
fun UserItem(
    user: User,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable  { mutableStateOf(false) }
    Card(
        modifier = modifier
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
                UserInformation(user, modifier = Modifier.align(Alignment.Top).weight(0.8f))
                UserItemButton(
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
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun UserItemButton(
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
@Preview
@Composable
fun ActionButtonGroup(modifier: Modifier = Modifier) {
    val options = listOf(
        Triple(Color.Green, Icons.AutoMirrored.Outlined.Login) {},
        Triple(Color.Cyan, Icons.Outlined.Info) {},
        Triple(Color.Yellow, Icons.Outlined.Edit) {},
        Triple(Color.Red, Icons.Outlined.Delete) {},
    )



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
                label =
                {
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
        UserList(
            listOf(
                User(role = Role.ADMIN, firstName = "Fernando", lastName = "Campestrini".repeat(50)),
                User(role = Role.ADMIN, firstName = "German", lastName = "Fernandez"),
                User(role = Role.ADMIN, firstName = "Natalia", lastName = "Rivera"),
                User(role = Role.ADMIN, firstName = "Pedro", lastName = "Cruz"),
            )
        )
    }
}

/**
 * Composable that displays what the UI of the app looks like in dark theme in the design tab.
 */
@Preview(showBackground = true)
@Composable
fun UserListDarkPreview() {
    CedicaTheme(darkTheme = true) {
        UserList(
            listOf(
                User(role = Role.ADMIN, firstName = "Fernando", lastName = "Campestrini".repeat(50)),
                User(role = Role.ADMIN, firstName = "German", lastName = "Fernandez"),
                User(role = Role.ADMIN, firstName = "Natalia", lastName = "Rivera"),
                User(role = Role.ADMIN, firstName = "Pedro", lastName = "Cruz"),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    CedicaTheme {
        UserItem(
            user = User(role = Role.ADMIN, firstName = "Fernando", lastName = "Campestrini")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemButtonPreview() {
    CedicaTheme {
        UserItemButton(
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
            user = User(role = Role.ADMIN, firstName = "Fernando", lastName = "Campestrini")
        )
    }
}