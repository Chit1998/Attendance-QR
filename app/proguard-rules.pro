# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.hktpl.attandanceqr.** {*;}
-keepclassmembers class com.hktpl.attandanceqr.** { *; }
-keep class com.hktpl.attandanceqr.ui.** {*;}
-keep class com.hktpl.attandanceqr.adapter.** {*;}
-keep class com.hktpl.attandanceqr.di.** {*;}
-keep class com.hktpl.attandanceqr.fragments.** {*;}
-keep class com.hktpl.attandanceqr.internet.** {*;}
-keep class com.hktpl.attandanceqr.internet.objects.** {*;}
-keep class com.hktpl.attandanceqr.models.** {*;}
-keep class com.hktpl.attandanceqr.objects.** {*;}
-keep class com.hktpl.attandanceqr.peferences.** {*;}
-keep class com.hktpl.attandanceqr.services.** {*;}
-keep class com.hktpl.attandanceqr.viewModels.** {*;}
-keep class com.hktpl.attandanceqr.di.repo.** {*;}


-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
-keepattributes *Annotation*
-dontwarn retrofit2.**


-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclasseswithmembers class * {
    <init>(...);
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken