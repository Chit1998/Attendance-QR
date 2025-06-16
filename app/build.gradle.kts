plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.hktpl.attandanceqr"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hktpl.attandanceqr"
        minSdk = 23
        //noinspection EditedTargetSdkVersion
        targetSdk = 36
        versionCode = 1
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    ndkVersion = "29.0.13599879"

    externalNativeBuild {
        cmake {
            version = "4.0.2"
        }
        ndkBuild {
            path("src/main/jni/Android.mk") //path of Android.mk file
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    TODO default splash screen
    implementation (libs.androidx.core.splashscreen)
//    TODO Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.converter.scalars)
    implementation (libs.okhttp)
    implementation (libs.rxjava)
    implementation(libs.kotlinx.coroutines.core)
    implementation (libs.adapter.rxjava2)
    implementation(libs.logging.interceptor)

//    TODO: Swipe Refresh layout
    implementation (libs.androidx.swiperefreshlayout)

//   TODO: hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.savedstate)
    implementation (libs.androidx.lifecycle.extensions)
    kapt (libs.androidx.lifecycle.compiler)

//    TODO: firebase
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.messaging.ktx)

//    TODO Google GSM
    implementation(libs.gms.play.services.gcm)

//    TODO Lottie
    implementation(libs.lottie)

//    TODO QR Scanner
    implementation (libs.zxing.android.embedded)
    implementation (libs.core)
}