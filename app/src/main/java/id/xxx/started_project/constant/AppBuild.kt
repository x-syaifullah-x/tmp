package id.xxx.started_project.constant

import id.xxx.started_project.BuildConfig

object AppBuild {

    const val IS_DEBUG = BuildConfig.BUILD_TYPE == "debug"

    const val APPLICATION_ID = BuildConfig.APPLICATION_ID

    const val FILE_PROVIDER_AUTHORITIES = "$APPLICATION_ID.FILE_PROVIDER"
}