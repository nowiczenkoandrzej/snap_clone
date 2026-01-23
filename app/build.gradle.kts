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
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.bundles.compose.debug)

    testImplementation(libs.bundles.test.unit)
    androidTestImplementation(libs.bundles.test.android)

    implementation(libs.bundles.koin)

    testImplementation(libs.bundles.test.unit)
    androidTestImplementation(libs.bundles.test.android)


    implementation(libs.accompanist.permissions)


    implementation(project(":core_editor"))
    implementation(project(":core_ui"))
    implementation(project(":core_saving"))
    implementation(project(":feature_canvas"))
    implementation(project(":feature_image_editing"))
    implementation(project(":feature_text"))
    implementation(project(":feature_stickers"))
    implementation(project(":feature_drawing"))

}