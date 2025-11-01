// File: app/src/main/kotlin/com/maverick.week7soal2.ui.theme/Theme.kt

package com.maverick.week7soal2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// --- Impor dari Color.kt dan Type.kt ---
import com.maverick.week7soal2.ui.theme.OrangeRetro
import com.maverick.week7soal2.ui.theme.PrimaryDark
import com.maverick.week7soal2.ui.theme.SecondaryDark
import com.maverick.week7soal2.ui.theme.Typography // Impor objek Typography
// ----------------------------------------

// Skema Dark Kustom (Mengganti skema default yang Anda definisikan di Type.kt)
private val DarkCustomColorScheme = darkColorScheme(
    primary = OrangeRetro,
    secondary = PrimaryDark,
    tertiary = OrangeRetro,
    background = SecondaryDark,
    surface = PrimaryDark,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFFB4934)
)

// Skema Light Default (Disederhanakan, jika tidak diperlukan)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6650a4),
    secondary = Color(0xFF625b71),
    tertiary = Color(0xFF7D5260)
)


@Composable
fun ArtistExplorerTheme( // MENGGANTI Week7Soal2Theme menjadi ArtistExplorerTheme
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Dinonaktifkan karena menggunakan skema warna kustom Dark
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Jika Anda ingin mendukung Dynamic Color pada skema Light default
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // Selalu gunakan skema Dark kustom yang kita definisikan untuk tema utama
        else -> DarkCustomColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Sekarang merujuk objek dari Type.kt
        content = content
    )
}