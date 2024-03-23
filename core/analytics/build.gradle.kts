plugins {
  alias(libs.plugins.troona.android.library)
  alias(libs.plugins.troona.android.library.compose)
  alias(libs.plugins.troona.android.hilt)
}

android {
  namespace = "com.donfreddy.troona.core.analytics"
}

dependencies {
  implementation(libs.androidx.compose.runtime)

  //prodImplementation(platform(libs.firebase.bom))
  //prodImplementation(libs.firebase.analytics)
}