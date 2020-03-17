# Gradle git flow version plugin

## Description
This plugin increments version number in project file version.properties. If there is no such file it is created.
Structure of this file is:

`app.version.major=1
app.version.minor=0
app.version.patch=0`

If plugin is applied

`apply(plugin = "com.etherealscope.gradlegitflowversionplugin")`

project.version is set to 1.0.0.STAGE, where STAGE is taken from current git branch
`when (branch) {
    master -> STAGE = RELEASE
    release/* -> STAGE = RC
    else -> STAGE = SNAPSHOT-yyMMdd-HHmm
}`

## Tasks
**changeVersion**
optional param _--scope_, default value MINOR, possible values MAJOR, MINOR, PATCH
Task increments version in version.properties file.

**startRelease**
optional param _--scope_, default MINOR, possible values MAJOR, MINOR
Task increments version, creates branch release/VERSION from develop and push to origin

**startFeature**
optional param _--featureName_
Task creates branch feature/featureName from develop and push to origin

**startHotfix**
Task increments patch version, creates branch hotfix/VERSION from master and push to origin