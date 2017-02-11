# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keep class cn.xiaoneng.** { *; }
-keep class edu.emory.** { *; }
-keep class net.sf.retrotranslator.** { *; }
-keep class org.fusesource.** { *; }

-dontwarn cn.xiaoneng.**
-dontwarn edu.emory.**
-dontwarn net.sf.retrotranslator.**
-dontwarn org.fusesource.**
