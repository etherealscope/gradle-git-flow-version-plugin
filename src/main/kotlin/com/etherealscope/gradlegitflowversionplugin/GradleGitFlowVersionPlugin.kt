package com.etherealscope.gradlegitflowversionplugin

import com.etherealscope.gradlegitflowversionplugin.task.ChangeVersionTask
import com.etherealscope.gradlegitflowversionplugin.task.StartFeatureTask
import com.etherealscope.gradlegitflowversionplugin.task.StartHotfixTask
import com.etherealscope.gradlegitflowversionplugin.task.StartReleaseTask
import com.etherealscope.gradlegitflowversionplugin.version.VersionUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

public class GradleGitFlowVersionPlugin : Plugin<Project> {

    public override fun apply(project: Project) {
        val versionPropertyFile = File(project.projectDir, "version.properties")
        val versionUtil = VersionUtil(versionPropertyFile, project)
        project.version = versionUtil.currentVersionString()

        project.tasks.create<StartReleaseTask>("startRelease", StartReleaseTask::class.java).run {
            description = "Print test message"
            group = "GradleGitFlowVersionPlugin"
        }

        project.tasks.create<StartFeatureTask>("startFeature", StartFeatureTask::class.java).run {
            description = "Print test message"
            group = "GradleGitFlowVersionPlugin"
        }

        project.tasks.create<StartHotfixTask>("startHotfix", StartHotfixTask::class.java).run {
            description = "Print test message"
            group = "GradleGitFlowVersionPlugin"
        }

        project.tasks.create<ChangeVersionTask>("changeVersion", ChangeVersionTask::class.java).run {
            description = "Print test message"
            group = "GradleGitFlowVersionPlugin"
        }
    }

}