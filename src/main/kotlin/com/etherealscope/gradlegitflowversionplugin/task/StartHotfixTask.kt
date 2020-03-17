package com.etherealscope.gradlegitflowversionplugin.task

import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.HOTFIX
import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.MASTER
import com.etherealscope.gradlegitflowversionplugin.git.GitUtil
import com.etherealscope.gradlegitflowversionplugin.version.VersionScope.PATCH
import com.etherealscope.gradlegitflowversionplugin.version.VersionStage.RC
import com.etherealscope.gradlegitflowversionplugin.version.VersionUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

public class StartHotfixTask: DefaultTask() {

    @TaskAction
    fun testAction() {
        println("Starting hotfix")

        val versionPropertyFile = File(project.projectDir, "version.properties")
        val versionUtil = VersionUtil(versionPropertyFile, project)
        val gitUtil = GitUtil()

        if (gitUtil.hasUntrackedFiles()) {
            throw IllegalStateException("Branch has untracked files")
        }

        if (gitUtil.getGitBranchType() != MASTER) {
            gitUtil.switchToBranch("master")
        }

        versionUtil.changeVersion(PATCH)
        gitUtil.createUpstreamBranch(HOTFIX.prefix + versionUtil.currentVersionStringWithoutStage())
        gitUtil.pushVersionFile(versionUtil)

        println("Hotfix created")
    }

}