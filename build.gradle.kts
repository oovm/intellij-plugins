import org.gradle.api.tasks.Copy

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.intelliJPlatform) apply false
    alias(libs.plugins.intelliJPlatformModule) apply false
    alias(libs.plugins.changelog) apply false
    alias(libs.plugins.qodana) apply false
}

val marketplacePluginProjects: List<Project> =
    rootProject.findProject(":plugins")
        ?.childProjects
        ?.values
        ?.sortedBy { it.path }
        ?: emptyList()

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    register("ciVerify") {
        group = "verification"
        description = "CI gate: compile, compile tests, and package each Marketplace plugin separately."
        dependsOn(
            ":plugins:intellij-fluent:ciVerify",
            ":plugins:intellij-jss:ciVerify",
            ":plugins:intellij-wit:ciVerify",
        )
    }

    register<Delete>("cleanCollectedPluginZips") {
        group = "build"
        description = "Remove previously collected plugin zips from build/."
        delete(fileTree(layout.buildDirectory).matching { include("*.zip") })
    }

    register<Copy>("collectPluginZips") {
        group = "build"
        description = "Copy plugin zips from each :plugins/* module into the repo-root build/ directory."
        dependsOn("cleanCollectedPluginZips")
        dependsOn(marketplacePluginProjects.map { "${it.path}:buildPlugin" })
        mustRunAfter("cleanCollectedPluginZips")
        marketplacePluginProjects.forEach { pluginProject ->
            from(pluginProject.layout.buildDirectory.dir("distributions")) {
                include("${pluginProject.name}-${pluginProject.version}.zip")
            }
        }
        into(layout.buildDirectory)
    }

    register("buildPlugins") {
        group = "build"
        description = "Build every Marketplace plugin zip and collect them under build/."
        dependsOn("collectPluginZips")
    }

    register("runIde") {
        group = "intellij"
        description = "Run IDE with every `:plugins/*` Marketplace plugin loaded together."
        dependsOn(":plugins:intellij-fluent:runIde")
    }

    register("publishLibrariesToMavenLocal") {
        group = "publishing"
        description = "Publish `:packages` to the local Maven repository."
        dependsOn(":packages:publishToMavenLocal")
    }
}
