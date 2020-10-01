package com.etherealscope.gradlegitflowversionplugin.git

import com.etherealscope.gradlegitflowversionplugin.git.GitBranchType.OTHER
import com.etherealscope.gradlegitflowversionplugin.version.VersionUtil
import java.io.File
import java.io.IOException
import java.lang.ProcessBuilder.Redirect.PIPE
import java.util.concurrent.TimeUnit.SECONDS

public class GitUtil {

    fun pushVersionFile(versionUtil: VersionUtil) {
        "git add version.properties".runCommand()
        "git commit -m 'version.properties (%s) [skip ci]'".format(versionUtil.currentVersionString()).runCommand()
        "git push".runCommand()
    }

    fun createUpstreamBranch(branch: String) {
        "git branch %s".format(branch).runCommand()
        "git checkout %s".format(branch).runCommand()
        "git push --set-upstream origin %s".format(branch).runCommand()
    }

    fun switchToBranch(branch: String) {
        "git checkout %s".format(branch).runCommand()
    }

    fun getGitBranchType(): GitBranchType {
        val currentBranch = getCurrentGitBranch() ?: return OTHER
        for (item in GitBranchType.values()) {
            if (currentBranch.startsWith(item.prefix, true))
                return item
        }
        return OTHER
    }

    fun getCurrentGitBranch(): String? {
        return "git rev-parse --abbrev-ref HEAD".runCommand()
    }

    fun getGitDiff(branch: String): String? {
        return "git log %s.. --oneline".format(branch).runCommand()
    }

    fun hasUntrackedFiles(): Boolean {
        return !"git diff-index HEAD".runCommand().isNullOrBlank()
    }

    fun shortCommitId(): String {
        return "git rev-parse --short HEAD".runCommand().orEmpty()
    }

    private fun String.runCommand(): String? {
        try {
            val parts = this.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                    .directory(File("./"))
                    .redirectOutput(PIPE)
                    .redirectError(PIPE)
                    .start()

            proc.waitFor(60, SECONDS)
            val result = proc.inputStream.bufferedReader().readText()
            println(result)
            return result.trim()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}