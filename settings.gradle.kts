pluginManagement {
    repositories {
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/gradle-plugins/") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        gradlePluginPortal()
    }
}

rootProject.name = "Intellij Plugins"

// `:packages` is the single shared library (no nested children).
include("packages")
project(":packages").projectDir = file("projects/packages")

include(
    "plugins",
    "plugins:intellij-fluent",
    "plugins:intellij-jss",
    "plugins:intellij-wit",
)
project(":plugins").projectDir = file("projects/plugins")
project(":plugins:intellij-fluent").projectDir = file("projects/plugins/intellij-fluent")
project(":plugins:intellij-jss").projectDir = file("projects/plugins/intellij-jss")
project(":plugins:intellij-wit").projectDir = file("projects/plugins/intellij-wit")
