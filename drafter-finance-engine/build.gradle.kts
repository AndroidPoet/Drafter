import io.androidpoet.drafter.Configuration
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
}

// The canonical, UI-free charting engine. It deliberately depends on NOTHING but
// the Kotlin stdlib — no Compose, no androidx — so it is the cross-language spec
// that the Compose / SwiftUI / Flutter / React Native renderers all mirror.
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
    nodejs()
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
    nodejs()
  }

  sourceSets {
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }

  explicitApi()
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "io.androidpoet.drafter.finance.engine"
  defaultConfig {
    minSdk = Configuration.minSdk
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  lint {
    abortOnError = false
  }
}
