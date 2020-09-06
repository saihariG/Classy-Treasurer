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
-keep class com.classyinc.classytreasurer.Model.** {*;}
-keep class com.classyinc.classytreasurer.confg.** {*;}
-keep class com.classyinc.classytreasurer.Utils.** {*;}

-keep class com.classyinc.classytreasurer.** {*;}

-keep class com.classyinc.classytreasurer.adapters.SearchAdapter {*;}
-keep class com.classyinc.classytreasurer.adapters.SearchexpenseAdapter {*;}
-keep class com.classyinc.classytreasurer.activities.RewardActivity {*;}
-keep class com.classyinc.classytreasurer.activities.ResetActivity {*;}
-keep class com.classyinc.classytreasurer.activities.RequestFeature {*;}
-keep class com.classyinc.classytreasurer.activities.RegistrationActivity {*;}

-keep class com.classyinc.classytreasurer.activities.MainActivity {*;}
-keep class com.classyinc.classytreasurer.fragments.IncomeFragment {*;}
-keep class com.classyinc.classytreasurer.activities.HomeActivity {*;}
-keep class com.classyinc.classytreasurer.fragments.ExpenseFragment {*;}
-keep class com.classyinc.classytreasurer.activities.Developer {*;}
-keep class com.classyinc.classytreasurer.fragments.DashBoardFragment {*;}

-keep class *{
      public private *;
}