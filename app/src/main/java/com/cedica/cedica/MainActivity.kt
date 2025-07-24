package com.cedica.cedica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cedica.cedica.core.navigation.NavigationWrapper
import com.cedica.cedica.ui.theme.CedicaTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CedicaTheme {
                NavigationWrapper()
            }
        }
    }
}

