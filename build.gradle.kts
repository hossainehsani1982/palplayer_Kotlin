buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("io.realm:realm-gradle-plugin:10.11.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
plugins {
    id("io.realm.kotlin") version "1.8.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    //id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
