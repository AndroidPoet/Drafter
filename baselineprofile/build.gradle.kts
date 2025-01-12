import io.androidpoet.drafter.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(
    libs.plugins.android.test
      .get()
      .pluginId,
  )
  id(
    libs.plugins.kotlin.android
      .get()
      .pluginId,
  )
  id(
    libs.plugins.baseline.profile
      .get()
      .pluginId,
  )
}

android {
  namespace = "io.androidpoet.drafter.baselineprofile"
  compileSdk = Configuration.compileSdk

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = libs.versions.jvmTarget.get()
  }

  defaultConfig {
    minSdk = 24
    targetSdk = Configuration.targetSdk
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  targetProjectPath = ":baselineprofile-app"

  testOptions.managedDevices.devices {
    maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6api31").apply {
      device = "Pixel 6"
      apiLevel = 31
      systemImageSource = "aosp"
    }
  }
}
baselineProfile {
  managedDevices += "pixel6api31"
  useConnectedDevices = true
}

dependencies {
  implementation(libs.androidx.test.runner)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.macro)
  implementation(libs.androidx.profileinstaller)
}
