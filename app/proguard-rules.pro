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

-dontwarn okio.**
-keep class okio.** { *;}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}

-dontwarn com.ximalaya.ting.android.player.**
-keep class com.ximalaya.ting.android.player.** { *;}

-dontwarn com.google.gson.**
-keep class com.google.gson.** {*;}

-dontwarn org.litepal.**
-keep class org.litepal.** {*;}

-keep class com.ximalaya.ting.android.opensdk.** {*;}
-keep interface com.ximalaya.ting.android.opensdk.** {*;}
-keep public enum com.ximalaya.ting.android.opensdk.** { *; }