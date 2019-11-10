# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/mohammadmehdi/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class mediaName to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file mediaName.
#-renamesourcefileattribute SourceFile
-dontwarn com.mikepenz.fastadapter_extensions.**
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class net.jhoobin.jhub.json.** { *; }
-dontwarn android.net.http.HttpResponseCache
-dontwarn com.beloo.widget.chipslayoutmanager.**