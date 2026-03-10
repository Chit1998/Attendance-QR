// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies{
        classpath(libs.secrets.gradle.plugin)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.ksp) apply false
//    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    id("com.google.devtools.ksp") version "2.3.10-2.0.2"
    id ("org.jetbrains.kotlin.jvm") version "2.3.10" apply false
    id("com.google.dagger.hilt.android") version "2.59.2" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
}