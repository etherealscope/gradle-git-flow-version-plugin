package com.etherealscope.gradlegitflowversionplugin.version

import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType
import com.etherealscope.gradlegitflowversionplugin.git.GitUtil
import com.etherealscope.gradlegitflowversionplugin.version.VersionScope.*
import com.etherealscope.gradlegitflowversionplugin.version.VersionStage.*
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            GitBranchType.RELEASE -> return RC.toString()
            else -> return SNAPSHOT.toString() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmm"))
        }
    }

    fun currentVersionString(): String {
        return (currentVersionStringWithoutStage() + "." + getStageVersion())
    }

    fun currentVersionStringWithoutStage(): String {
        return (getMajorVersion() + "." + getMinorVersion() + "." + getPatchVersion())
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