package com.etherealscope.gradlegitflowversionplugin.version

import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType
import com.etherealscope.gradlegitflowversionplugin.git.GitUtil
import com.etherealscope.gradlegitflowversionplugin.version.VersionScope.*
import com.etherealscope.gradlegitflowversionplugin.version.VersionStage.*
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

public class VersionUtil(val versionPropertyFile: File, val project: Project) {

    val majorKey: String = "app.version.major"
    val minorKey: String = "app.version.minor"
    val patchKey: String = "app.version.patch"

    val versionProps: Properties = Properties()
    val gitUtil: GitUtil = GitUtil()

    init {
        if (!versionPropertyFile.exists()) {
            versionProps[majorKey] = "1"
            versionProps[minorKey] = "0"
            versionProps[patchKey] = "0"
            versionProps.store(FileOutputStream(versionPropertyFile), null)
            versionProps.load(FileInputStream(versionPropertyFile))
        } else {
            versionProps.load(FileInputStream(versionPropertyFile))
        }
        project.version = currentVersionString()
    }

    fun getMajorVersion(): String {
        return versionProps.getProperty(majorKey)
    }

    fun getMinorVersion(): String {
        return versionProps.getProperty(minorKey)
    }

    fun getPatchVersion(): String {
        return versionProps.getProperty(patchKey)
    }

    fun getStageVersion(): String {
        when (gitUtil.getGitBranchType()) {
            GitBranchType.MASTER -> return RELEASE.toString()
            GitBranchType.RELEASE, GitBranchType.HOTFIX -> return RC.toString() + "-" + gitUtil.shortCommitId()
            GitBranchType.DEVELOP -> return SNAPSHOT.toString() + "-" + gitUtil.shortCommitId()
            GitBranchType.FEATURE -> return M.toString() + "-" + parseFeatureName() + "-" + gitUtil.shortCommitId()
            else -> return SNAPSHOT.toString() + "-" + gitUtil.shortCommitId()
        }
    }

    fun currentVersionString(): String {
        return (currentVersionStringWithoutStage() + "." + getStageVersion())
    }

    fun currentVersionStringWithoutStage(): String {
        return (getMajorVersion() + "." + getMinorVersion() + "." + getPatchVersion())
    }

    fun parseFeatureName(): String {
        val featureName = gitUtil.getCurrentGitBranch()?.substringAfter("feature/")?.replace("[^a-zA-Z0-9-]".toRegex(), "").orEmpty()
        if (featureName.isEmpty()) {
            return featureName
        }
        return featureName.substring(0, Math.min(featureName.length, 15))
    }

    fun changeVersion(scope: VersionScope) {
        if (scope == VersionScope.NONE) {
            return
        }

        when (scope) {
            MAJOR -> {
                versionProps[majorKey] = (getMajorVersion().toInt() + 1).toString()
                versionProps[minorKey] = "0"
                versionProps[patchKey] = "0"
            }
            MINOR -> {
                versionProps[minorKey] = (getMinorVersion().toInt() + 1).toString()
                versionProps[patchKey] = "0"
            }
            PATCH -> versionProps[patchKey] = (getPatchVersion().toInt() + 1).toString()
        }

        versionProps.store(FileOutputStream(versionPropertyFile), null)
        versionProps.load(FileInputStream(versionPropertyFile))
        project.version = currentVersionString()
    }

}