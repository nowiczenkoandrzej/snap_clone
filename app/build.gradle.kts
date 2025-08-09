plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.an.facefilters"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.an.facefilters"
        minSdk = 30
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.bundles.androidx.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    testImplementation(libs.bundles.test.unit)
    androidTestImplementation(libs.bundles.test.android)
    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    testImplementation(libs.org.mockito.mockito.core)
    androidTestImplementation(libs.org.mockito.mockito.android)
    testImplementation(libs.mockito.mockito.kotlin)

    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.compose)
    

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.accompanist.permissions)

    implementation(libs.colorpicker.compose)

    implementation(libs.play.services.mlkit.subject.segmentation)


    implementation(project(":core_editor"))
    implementation(project(":core_ui"))
    implementation(project(":feature_canvas"))

}