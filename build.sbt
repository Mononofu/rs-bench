scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-25"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

libraryDependencies ++=
  "com.android.support" % "appcompat-v7" % "24.0.0" ::
  "com.android.support.test" % "runner" % "0.5" % "androidTest" ::
  "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest" ::
  Nil

rsTargetApi in Android := "24"
rsSupportMode in Android := true

proguardOptions in Android ++=
  "-keep class android.support.v8.renderscript.** { *; }" ::
  Nil
