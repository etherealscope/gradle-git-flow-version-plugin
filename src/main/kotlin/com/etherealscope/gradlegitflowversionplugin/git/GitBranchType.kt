package com.etherealscope.gradlegitflowversionplugin.git

public enum class GitBranchType(val prefix: String) {
    MASTER("master"),
    DEVELOP("develop"),
    RELEASE("release/"),
    FEATURE("feature/"),
    HOTFIX("hotfix/"),
    OTHER("")
}