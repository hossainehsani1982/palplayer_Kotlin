plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("io.realm.kotlin")
    kotlin("kapt")

}

android {
    namespace = "com.hossainehs.palplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hossainehs.palplayer"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //lifecycle scope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    //screen window
    implementation("androidx.window:window:1.2.0-alpha03")

    //Dagger hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.44.2")

    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //live data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //fragment
    implementation("androidx.fragment:fragment-ktx:1.6.0")


    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //Room
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    //lottie
    implementation("com.airbnb.android:lottie:3.6.0")

    //Realm
    implementation("io.realm.kotlin:library-base:1.8.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.0")
    implementation("androidx.paging:paging-common-ktx:3.2.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //MediaBrowserServiceCompat
    implementation("androidx.media:media:1.6.0")

    //LifecycleService
    implementation ("androidx.lifecycle:lifecycle-service:2.6.1")

    //ExoPlayer
    api ("com.google.android.exoplayer:exoplayer-core:2.19.0")
    api ("com.google.android.exoplayer:exoplayer-ui:2.19.0")
    api ("com.google.android.exoplayer:extension-mediasession:2.19.0")






}