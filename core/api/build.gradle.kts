dependencies {
    api(project(":backend:state"))

    implementation(project(":utils"))
    implementation(Deps.Kotlin.SERIALIZATION)
}