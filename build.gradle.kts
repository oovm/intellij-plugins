plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.intelliJPlatform) apply false
    alias(libs.plugins.intelliJPlatformModule) apply false
    alias(libs.plugins.changelog) apply false
    alias(libs.plugins.qodana) apply false
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    register("ciVerify") {
        group = "verification"
        description = "CI gate: compile, compile tests, and package each Marketplace plugin separately."
        dependsOn(":plugins:intellij-fluent:ciVerify")
    }

    register("buildPlugins") {
        group = "build"
        description = "Build every Marketplace plugin zip separately for individual Marketplace upload."
        dependsOn(":plugins:intellij-fluent:buildPlugin")
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
