package id.xxx.module.koin

import id.xxx.module.ui.viewmodel.SignViewModel
import id.xxx.module.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AuthPresentation {

    private val viewModel = module {
        viewModel { SignViewModel(get()) }
        viewModel { UserViewModel() }
    }

    val modules = listOf(viewModel)
}