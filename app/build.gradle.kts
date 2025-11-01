plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.maverick.week7soal2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maverick.week7soal2"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    // WAJIB: Memaksa Resolusi Versi Tunggal yang TINGGI
    configurations.all {
        resolutionStrategy {
            // Definisikan konstanta di sini
            val lifecycleVersion = "2.8.3"
            val coroutinesVersion = "1.8.0"
            val activityVersion = "1.9.0"
            val navVersion = "2.7.5"
            val coreVersion = "1.13.1"
            val collectionVersion = "1.4.2"

            // Memaksa versi untuk komponen inti
            force("androidx.activity:activity-ktx:$activityVersion")
            force("androidx.activity:activity-compose:$activityVersion")

            force("androidx.navigation:navigation-compose:$navVersion")

            // Memaksa semua komponen Lifecycle
            force("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
            force("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
            force("androidx.lifecycle:lifecycle-common:$lifecycleVersion")
            force("androidx.lifecycle:lifecycle-runtime:$lifecycleVersion")

            // Memaksa komponen AndroidX umum yang sering berkonflik
            force("androidx.core:core-ktx:$coreVersion")
            force("androidx.collection:collection-ktx:$collectionVersion")

            // Memaksa Coroutines
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            force("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
        }
    }
}

dependencies {

    // --- DEPENDENSI COMPOSE/ANDROIDX YANG MINIMAL ---

    // WAJIB: Platform Compose BOM (Mengatur semua versi Compose/AndroidX)
    implementation(platform(libs.androidx.compose.bom))

    // Diatur oleh libs.versions.toml
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigasi & Activity (Menggunakan versi eksplisit 1.9.0)
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Lifecycle (Menggunakan versi eksplisit 2.8.3)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-common:2.8.3")

    // Retrofit (Networking)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil (Image Loading)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Testing (tetap sama)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}