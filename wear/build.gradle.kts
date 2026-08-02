plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.kristianolsson.weeknow.wear"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kristianolsson.weeknow"
        minSdk = 28
        targetSdk = 36
        versionCode = 16
        versionName = "1.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.wear.complications.data.source.ktx)
    implementation(libs.wear)
    implementation(libs.play.services.wearable)
}
