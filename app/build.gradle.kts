import java.security.KeyException
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

val apiKeyFile = "apikey.properties"
val apiKeyDefaultsFile = "apikeydefault.properties"
val apiKeyField = "PEXELS_API_KEY"

android {
    namespace = "dev.catsuperberg.pexels.app"
    compileSdk = 34

    android.buildFeatures.buildConfig = true
    defaultConfig {
        applicationId = "dev.catsuperberg.pexels.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", apiKeyField, retrieveApiKey())

        ksp { arg("room.schemaLocation", "$projectDir/schemas") }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.9.54")
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("joda-time:joda-time:2.12.5")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.9.54")
    ksp("com.google.dagger:dagger-compiler:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")
    ksp("androidx.room:room-compiler:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

fun retrieveApiKey(): String {
    val properties = Properties()
    val apiKeyFile = layout.projectDirectory.file(apiKeyFile)
    if (apiKeyFile.asFile.exists().not())
        resetApiKeyFile()
    properties.load(apiKeyFile.asFile.reader())
    val key = properties.getProperty(apiKeyField)
    if(key == "\"\"")
        throw ProjectConfigurationException("Provide a valid API key in: $apiKeyFile", KeyException())
    return key
}

fun resetApiKeyFile() {
    copy {
        val origin = layout.projectDirectory.file(apiKeyDefaultsFile)
        val destination = layout.projectDirectory
        from(origin)
        rename("(.*)", apiKeyFile)
        into(destination)

        println("resetApiKeysFile executed on $origin in $destination")
        val destinationFile = destination.file(apiKeyFile)
        println("$destinationFile exists: ${destinationFile.asFile.exists()}")
    }
}
