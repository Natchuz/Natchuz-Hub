import org.gradle.api.Project

fun Project.projects(vararg projects: String, body: Project.() -> Unit) {
    for (p in projects) {
        project(p, body)
    }
}