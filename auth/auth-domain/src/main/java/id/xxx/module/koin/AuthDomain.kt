package id.xxx.module.koin

import id.xxx.module.data.interactor.SignInteractorImpl
import org.koin.dsl.module

object AuthDomain {

    private val module = module {
        single { SignInteractorImpl.getInstance(get()) }
    }

    val modules = listOf(module)
}