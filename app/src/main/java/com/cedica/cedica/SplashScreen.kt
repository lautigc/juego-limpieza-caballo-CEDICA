package com.cedica.cedica

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cedica.cedica.core.navigation.Menu
import com.cedica.cedica.core.navigation.NavigationWrapper
import kotlinx.coroutines.delay

@Composable
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painter = painterResource(id = R.drawable.logo_clean), contentDescription = "Logo")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Aprendizaje didáctico", fontSize = 30.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SplashScreen(navigateToMenu: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(1000)
        navigateToMenu()
    }
    Splash()
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen {
    }
}