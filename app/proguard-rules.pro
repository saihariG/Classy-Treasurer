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
-keep class com.example.classytreasurer.Model.** {*;}
-keep class com.example.classytreasurer.confg.** {*;}
-keep class com.example.classytreasurer.Utils.** {*;}

-keep class com.example.classytreasurer.** {*;}

-keep class com.classyinc.classytreasurer.SearchAdapter {*;}
-keep class com.classyinc.classytreasurer.SearchexpenseAdapter {*;}
-keep class com.classyinc.classytreasurer.RewardActivity {*;}
-keep class com.classyinc.classytreasurer.ResetActivity {*;}
-keep class com.classyinc.classytreasurer.RequestFeature {*;}
-keep class com.classyinc.classytreasurer.RegistrationActivity {*;}
-keep class com.classyinc.classytreasurer.PaymentDetails {*;}
-keep class com.classyinc.classytreasurer.PayActivity {*;}
-keep class com.classyinc.classytreasurer.MainActivity {*;}
-keep class com.classyinc.classytreasurer.IncomeFragment {*;}
-keep class com.classyinc.classytreasurer.HomeActivity {*;}
-keep class com.classyinc.classytreasurer.ExpenseFragment {*;}
-keep class com.classyinc.classytreasurer.Developer {*;}
-keep class com.classyinc.classytreasurer.DashBoardFragment {*;}

-keep class *{
      public private *;
}