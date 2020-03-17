package com.etherealscope.gradlegitflowversionplugin.task

import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.DEVELOP
import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.FEATURE
import com.etherealscope.gradlegitflowversionplugin.git.GitUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

public class StartFeatureTask: DefaultTask() {

    private var featureName: String? = null

    @Option(option = "feature-name", description = "Set feature name.")
    open fun setFeatureName(featureName: String) {
        this.featureName = featureName
    }

    @Input
    open fun getFeatureName(): String? {
        return featureName
    }

    @TaskAction
    fun testAction() {
        println("Starting feature")

        val gitUtil = GitUtil()

        if (gitUtil.hasUntrackedFiles()) {
            throw IllegalStateException("Branch has untracked files")
        }

        if (gitUtil.getGitBranchType() != DEVELOP) {
            gitUtil.switchToBranch("develop")
        }

        gitUtil.createUpstreamBranch(FEATURE.prefix + featureName)

        println("Feature created")
    }

}