import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.findByName("android")
                ?: error("Android extension not found. Apply an Android plugin first.")

            (extension as CommonExtension<*, *, *, *, *, *>).apply {
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}
