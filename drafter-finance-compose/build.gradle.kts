import io.androidpoet.drafter.Configuration
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
}

// The Compose Multiplatform renderer SDK. It is a THIN walker over the
// platform-agnostic display list produced by :drafter-finance-engine — the same
// list the SwiftUI / Flutter / React Native renderers consume.
kotlin {
  androidTarget {
    publishLibraryVariants("release")
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
    }
  }
  jvm("desktop")
  iosX64()
  iosArm64()
  iosSimulatorArm64()
  macosX64()
  macosArm64()
  js(IR) {
    browser()
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.ui)
        // Engine types appear in this module's public API (Candle, CandleStyle),
        // so expose them transitively to consumers.
        api(project(":drafter-finance-engine"))
      }
    }
  }

  explicitApi()
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "io.androidpoet.drafter.finance.compose"
  defaultConfig {
    minSdk = Configuration.minSdk
  }

  buildFeatures {
    compose = true
    buildConfig = false
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  lint {
    abortOnError = false
  }
}
