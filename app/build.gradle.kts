plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.hossainehs.palplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hossainehs.palplayer"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    //enable view binding
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //lifecycle scope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    //screen window
    implementation("androidx.window:window:1.2.0-alpha03")

    //Dagger hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //live data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")


    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //lottie
    implementation("com.airbnb.android:lottie:3.6.0")


    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-common-ktx:3.2.1")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //LifecycleService
    implementation ("androidx.lifecycle:lifecycle-service:2.6.2")

    //MediaBrowserServiceCompat
    val media3Version = "1.2.0"
    implementation("androidx.media3:media3-datasource-okhttp:$media3Version")
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.legacy:legacy-support-v4:1.0.0") // Needed MediaSessionCompat.Token

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}