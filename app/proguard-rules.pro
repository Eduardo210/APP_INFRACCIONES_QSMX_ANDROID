# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ingmtz/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-printconfiguration "build/outputs/mapping/configuration.txt"
-keepattributes LineNumberTable
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-keep class com.google.gson.annotations.SerializedName
-keep class retrofit2.** { *;}
-keep class okhttp3.** { *;}
-keep class org.simpleframework.xml.** { *;}
-keep class com.pos.sdk.** { *;}
-keep class com.basewin.** { *;}
-keep class com.google.android.gms.** { *;}
-keep class com.google.firebase.** { *;}
-keep class mx.qsistemas.infracciones.net.request_web.** { *; }
-keep class mx.qsistemas.infracciones.net.result_web.** { *; }

-keep interface retrofit2.** { *;}
-keep interface org.simpleframework.xml.** { *;}
-keep interface com.pos.sdk.** { *;}
-keep interface com.basewin.** { *;}
-keep interface com.google.android.gms.** { *;}
-keep interface com.google.firebase.** { *;}

-keepclassmembers enum * { *; }

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}

-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}