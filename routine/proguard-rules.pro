-dontwarn com.anbillon.routine.**
-keep class com.anbillon.routine.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @com.anbillon.routine.app.* <methods>;
}