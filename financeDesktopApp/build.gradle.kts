import org.jetbrains.compose.desktop.application.dsl.TargetFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("org.jetbrains.kotlin.jvm")
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  implementation(compose.desktop.currentOs)
  implementation(project(":drafter-finance-compose"))
}

compose.desktop {
  application {
    mainClass = "io.androidpoet.financedemo.FinanceMainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg)
      packageName = "DrafterFinanceDemo"
      packageVersion = "1.0.0"
    }
  }
}
