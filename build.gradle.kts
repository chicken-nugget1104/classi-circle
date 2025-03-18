plugins {
    id("java-library")
    id("application")
}

group = "net.minecraft"
version = "0.0.3A-pubtestfacesneww"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://m2.dv8tion.net/releases") }
}

val natives: Configuration by configurations.creating
natives.isTransitive = true

dependencies {
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl", version = "2.9.3")
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl_util", version = "2.9.3")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-windows")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-linux")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-osx")
    implementation(files("libs/DiscordIPC-0.4.jar"))
    implementation("org.json:json:20210307")
    implementation("org.slf4j:slf4j-api:1.7.36") // SLF4J API
    implementation("org.slf4j:slf4j-simple:1.7.36") // Basic implementation
}


task("runb", JavaExec::class) {
    jvmArgs = listOf("-Dorg.lwjgl.librarypath=${project.projectDir.toPath()}\\run\\natives")
    main = "com.mojang.minecraft.Minecraft"
    classpath = sourceSets["main"].runtimeClasspath
    workingDir("${project.projectDir.toPath()}\\run")
    dependsOn("extractNatives")
}

task("extractNatives", Copy::class) {
    dependsOn(natives)
    from(natives.map { zipTree(it) })
    into("${project.projectDir.toPath()}\\run\\natives")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

application {
    mainClassName = "com.mojang.minecraft.Minecraft"
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.mojang.minecraft.Minecraft"
        )
    }
}
