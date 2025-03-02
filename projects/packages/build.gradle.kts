plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intelliJPlatform)
    `maven-publish`
}

group = providers.gradleProperty("libraryGroup").get()
version = providers.gradleProperty("libraryVersion").get()

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
    implementation("com.google.protobuf:protobuf-java:3.25.5")
    implementation("com.google.protobuf:protobuf-kotlin:3.25.5")

    intellijPlatform {
        intellijIdeaUltimate(providers.gradleProperty("platformVersion"))
        bundledPlugins(
            providers.gradleProperty("platformBundledPlugins").map {
                it.split(',').map(String::trim).filter(String::isNotEmpty)
            },
        )
    }
}

// Shared library is code-only: no src/main/resources by contract.
sourceSets.named("main") {
    resources.setSrcDirs(emptyList<Any>())
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
