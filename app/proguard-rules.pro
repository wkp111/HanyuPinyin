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
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#百度文字识别
-keep class com.baidu.ocr.sdk.**{*;}
-dontwarn com.baidu.ocr.**

#讯飞语音
-keep class com.iflytek.**{*;}
-keepattributes Signature

#有道翻译
#-ignorewarnings
#-libraryjars libs/YoudaoBase.jar
#-libraryjars libs/YoudaoTranslateOnline.jar

-keep class com.youdao.sdk.ydtranslate.** { *;}
-keep class com.youdao.sdk.chdict.** { *;}
-keep class com.youdao.localtransengine.** { *;}
-keep class com.youdao.sdk.ydonlinetranslate.** { *;}

#有米广告
-keep public class com.mi.adtracker.MiAdTracker{ *; }