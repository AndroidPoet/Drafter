import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvm("desktop")

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
        implementation(compose.ui)
        implementation(libs.kotlinx.datetime)
        implementation(project(":drafter"))
        implementation(project(":drafter-finance-compose"))
      }
    }
  }
}
