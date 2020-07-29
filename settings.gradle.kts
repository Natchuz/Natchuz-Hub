pluginManagement {
    repositories {
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
}
rootProject.name = "natchuz-hub"
include("utils")
include("protocol")
include("service")
include("bungeecord")
include("paper")
include("core")

// servers
include("lobby")
//include("kitpvp") currently not in use