plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

group = "com.etherealscope"
version = "1.0.0.RELEASE"

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