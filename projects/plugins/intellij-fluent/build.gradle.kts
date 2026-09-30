import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intelliJPlatform)
    alias(libs.plugins.changelog)
    alias(libs.plugins.qodana)
}

group = project.property("pluginGroup") as String
version = project.property("pluginVersion") as String

kotlin {
    jvmToolchain(21)
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(project(":packages"))

    testImplementation(libs.junit)

    intellijPlatform {
        intellijIdeaUltimate(providers.gradleProperty("platformVersion"))
        bundledPlugins(
            providers.gradleProperty("platformBundledPlugins").map {
                it.split(',').map(String::trim).filter(String::isNotEmpty)
            },
        )
        plugins(
            providers.gradleProperty("platformPlugins").map {
                it.split(',').map(String::trim).filter(String::isNotEmpty)
            },
        )
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
}

intellijPlatform {
    val pluginVersionProvider = providers.provider { version.toString() }

    pluginConfiguration {
        version = pluginVersionProvider

        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map(::markdownToHTML)

        val changelog = project.changelog
        changeNotes = pluginVersionProvider.map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        channels = pluginVersionProvider.map {
            listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" })
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

changelog {
    path.set(layout.projectDirectory.file("CHANGELOG.md").asFile.invariantSeparatorsPath)
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

tasks {
    publishPlugin {
        dependsOn(patchChangelog)
    }

    named("buildSearchableOptions") {
        enabled = false
    }

    test {
        if (System.getProperty("regenerate") != null) {
            systemProperty("regenerate", System.getProperty("regenerate"))
        }
    }

    register("ciVerify") {
        group = "verification"
        description = "CI gate: compile, compile tests, and package the Fluent plugin."
        dependsOn("compileKotlin", "compileTestKotlin", "test", "buildPlugin")
    }
}

intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}

// Load every `:plugins/*` Marketplace plugin together in the IDE sandbox.
// Packaging stays isolated: this module's `buildPlugin` / `publishPlugin` only emit this plugin's zip.
apply(from = rootProject.file("gradle/ide-all-plugins.gradle.kts"))
