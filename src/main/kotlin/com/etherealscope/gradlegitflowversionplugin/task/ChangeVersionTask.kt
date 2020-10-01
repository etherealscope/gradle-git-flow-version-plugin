package com.etherealscope.gradlegitflowversionplugin.task

import com.etherealscope.gradlegitflowversionplugin.version.VersionScope
import com.etherealscope.gradlegitflowversionplugin.version.VersionScope.MINOR
import com.etherealscope.gradlegitflowversionplugin.version.VersionStage
import com.etherealscope.gradlegitflowversionplugin.version.VersionStage.NONE
import com.etherealscope.gradlegitflowversionplugin.version.VersionUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.options.OptionValues
import java.io.File


public open class ChangeVersionTask: DefaultTask() {

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
        println("Changing version")

        val versionPropertyFile = File(project.projectDir, "version.properties")
        val versionUtil = VersionUtil(versionPropertyFile, project)

        println("old version " + project.version)

        versionUtil.changeVersion(scope)

        println("new version " + project.version)
    }

}