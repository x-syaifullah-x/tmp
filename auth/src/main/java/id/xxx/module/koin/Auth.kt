package id.xxx.module.koin

import org.koin.core.module.Module

object Auth {

    val modules = mutableListOf<Module>().apply {
        addAll(AuthData.modules)
        addAll(AuthDomain.modules)
        addAll(AuthPresentation.modules)
    }.toList()
}