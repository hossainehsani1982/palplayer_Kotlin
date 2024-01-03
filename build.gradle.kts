buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
