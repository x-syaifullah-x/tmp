package id.xxx.module.koin

import id.xxx.module.data.repository.SignRepositoryImpl
import id.xxx.module.data.source.remote.auth.email.AuthEmailDataSourceRemote
import id.xxx.module.data.source.remote.auth.exchange.AuthExchangeService
import org.koin.dsl.module

object AuthData {

    private val dataSourceRemote = module {
        single { AuthEmailDataSourceRemote.getInstance() }
        single { AuthExchangeService() }
    }

    private val dataRepository = module {
        single { SignRepositoryImpl.getInstance(get()) }
    }

    val modules = listOf(
        dataSourceRemote, dataRepository
    )
}