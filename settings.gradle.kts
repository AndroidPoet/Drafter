pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}
rootProject.name = "DrafterDemo"
include(":app")
include(":drafter")
include(":baselineprofile-app")
include(":baselineprofile")
