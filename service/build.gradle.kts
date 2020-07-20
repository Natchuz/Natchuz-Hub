dependencies {
    implementation(project(":protocol"))
}

docker {
    name = "service"
}

tasks.jar {
    manifest.attributes["Main-Class"] = "com.natchuz.hub.service.ServiceMain"
}