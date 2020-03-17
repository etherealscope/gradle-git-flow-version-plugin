package com.etherealscope.gradlegitflowversionplugin.task

import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.DEVELOP
import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.RELEASE
import com.etherealscope.gradlegitflowversionplugin.git.GitUtil
import com.etherealscope.gradlegitflowversionplugin.version.VersionScope
import com.etherealscope.gradlegitflowversionplugin.version.VersionScope.*
import com.etherealscope.gradlegitflowversionplugin.version.VersionUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.options.OptionValues
import java.io.File

public open class StartReleaseTask: DefaultTask() {

    private var scope: VersionScope = MINOR

    @Option(option = "scope", description = "Set incrementation scope.")
    open fun setScope(scope: VersionScope) {
        this.scope = scope
    }

    @Input
    open fun getScope(): VersionScope? {
        return scope
    }

    @OptionValues("scope")
    open fun getAvailableScopes(): List<VersionScope?>? {
        return VersionScope.values().asList()
    }

    @TaskAction
    fun testAction() {
        println("Starting release")

        val versionPropertyFile = File(project.projectDir, "version.properties")
        val versionUtil = VersionUtil(versionPropertyFile, project)
        val gitUtil = GitUtil()

        if (gitUtil.hasUntrackedFiles()) {
            throw IllegalStateException("Branch has untracked files")
        }

        if (gitUtil.getGitBranchType() != DEVELOP) {
            gitUtil.switchToBranch("develop")
        }

        if (scope == MAJOR) {
            versionUtil.changeVersion(MAJOR)
        } else {
            versionUtil.changeVersion(NONE)
        }
        gitUtil.createUpstreamBranch(RELEASE.prefix + versionUtil.currentVersionStringWithoutStage())
        gitUtil.pushVersionFile(versionUtil)

        gitUtil.switchToBranch("develop")

        versionUtil.changeVersion(MINOR)
        gitUtil.pushVersionFile(versionUtil)

        println("Release created")
    }

}