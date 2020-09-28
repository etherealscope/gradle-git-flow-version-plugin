plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
    id("com.gradle.plugin-publish") version "0.10.1"
}

group = "com.etherealscope"
version = "1.0.1.RELEASE"

pluginBundle {
    website = "https://github.com/etherealscope/gradle-git-flow-version-plugin"
    vcsUrl = "https://github.com/etherealscope/gradle-git-flow-version-plugin.git"
    tags = listOf("version", "git-flow")
}

gradlePlugin {
    plugins {
        create("gradleGitFlowVersionPlugin") {
            id = "com.etherealscope.gradlegitflowversionplugin"
            displayName = "Git Flow Version Plugin"
            description = "Plugin for incrementing version in git flow"
            implementationClass = "com.etherealscope.gradlegitflowversionplugin.GradleGitFlowVersionPlugin"
        }
    }
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    implementation(gradleApi())
    implementation(localGroovy())

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}