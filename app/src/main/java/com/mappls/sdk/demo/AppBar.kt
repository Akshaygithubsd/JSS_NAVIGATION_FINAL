package com.mappls.sdk.demo

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBar(
    title: String,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = Modifier.height(72.dp)
//        navigationIcon = {
//            IconButton(onClick = onNavigationIconClick) {
//                Icon(
//                    imageVector = Icons.Default.Menu,
//                    contentDescription = "Toggle drawer"
//                )
//            }
//        }
    )
}