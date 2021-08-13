object Versions {
    const val MIN_SDK = 23
    const val COMPILE_SDK = 30
    const val TARGET_SDK = 30

    const val KOTLIN = "1.5.21"
}

object GradlePlugin {
    const val ANDROID = "com.android.tools.build:gradle:7.0.0"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
}

object Dependencies {
    const val KOTLIN_STDLIB_JDK7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
    const val KOTLIN_COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1"
    const val MOSHI = "com.squareup.moshi:moshi:1.9.3"
    const val MOSHI_KOTLIN = "com.squareup.moshi:moshi-kotlin:1.9.3"
    const val PICASSO = "com.squareup.picasso:picasso:2.8"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0"
}

object AndroidX {
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.3.1"
    const val CORE = "androidx.core:core:1.6.0"
    const val CORE_KTX = "androidx.core:core-ktx:1.1.0"
    const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.1.0"
    const val LEGACY_SUPPORT_V4 = "androidx.legacy:legacy-support-v4:1.0.0"
    const val LIFECYCLE_EXTENSION = "androidx.lifecycle:lifecycle-extensions:2.3.1"
    const val MATERIAL = "com.google.android.material:material:1.4.0"
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment:2.3.5"
    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui:2.3.5"
    const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:1.0.0"
}
