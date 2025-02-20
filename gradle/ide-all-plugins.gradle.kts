/**
 * Marketplace plugins under `:plugins/*` share one IDE sandbox when running,
 * but each module still builds and publishes its own zip independently.
 *
 * Apply from each marketplace plugin `build.gradle.kts` after the IntelliJ Platform plugin.
 */
import org.jetbrains.intellij.platform.gradle.tasks.RunIdeTask

val marketplacePluginProjects: List<Project> =
    rootProject.findProject(":plugins")
        ?.childProjects
        ?.values
        ?.sortedBy { it.path }
        ?: emptyList()

val siblingMarketplacePlugins: List<Project> =
    marketplacePluginProjects.filter { it.path != project.path }

// runIde / custom runIde* tasks: always load every marketplace plugin in the monorepo.
tasks.withType<RunIdeTask>().configureEach {
    siblingMarketplacePlugins.forEach { sibling ->
        plugins {
            localPlugin(sibling)
        }
    }
}
