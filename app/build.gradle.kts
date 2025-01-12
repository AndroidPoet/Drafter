
import io.androidpoet.drafter.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(
    libs.plugins.android.application
      .get()
      .pluginId,
  )
  id(
    libs.plugins.kotlin.android
      .get()
      .pluginId,
  )
  id(
    libs.plugins.compose.compiler
      .get()
      .pluginId,
  )
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "io.androidpoet.drafterdemo"
  defaultConfig {
    applicationId = "io.androidpoet.drafterdemo"
    minSdk = Configuration.minSdk
    targetSdk = Configuration.targetSdk
    versionCode = Configuration.versionCode
    versionName = Configuration.versionName
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = libs.versions.jvmTarget.get()
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  packaging {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }

  lint {
    abortOnError = false
  }
  buildTypes {
    getByName("release") {
      signingConfig = signingConfigs.getByName("debug")
    }
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material3)
  implementation(libs.kotlinx.datetime)
  implementation(project(":drafter"))
}
task("testClasses") {}
