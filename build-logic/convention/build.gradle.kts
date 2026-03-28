import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.apps.android.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.plugins.android.application.map { it.toString() }.let { "com.android.tools.build:gradle:${libs.versions.agp.get()}" })
    compileOnly(libs.plugins.kotlin.android.map { it.toString() }.let { "org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}" })
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "apps.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "apps.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "apps.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "apps.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
