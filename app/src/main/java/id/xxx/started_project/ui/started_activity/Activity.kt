package id.xxx.started_project.ui.started_activity

import android.content.Context
import android.os.Bundle
import id.xxx.module.data.Action
import id.xxx.module.data.model.TokenModel
import id.xxx.module.ui.base.BaseFragmentActivityAuth
import id.xxx.module.ui.fragment.sign.SignFragment
import id.xxx.module.ui.fragment.sign.SignFragmentListener
import id.xxx.module.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Activity : BaseFragmentActivityAuth(), SignFragmentListener {

    companion object {
        const val CLASS_NAME = "StartedActivity"

        const val EXTRA_ACTION_MODE = SignFragment.EXTRA_ACTION_MODE

        fun registerReceiverAuthAction(context: Context?, onReceive: (Action) -> Unit) =
            SignFragment.registerReceiverAuthAction(context, onReceive)
    }

    private val userViewModel by viewModel<UserViewModel>()

    val container: Int = android.R.id.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            showSplash(
                delayMillis = 2000,
                onShowing = { userViewModel.setUser(this) },
                onFinished = { observerUser() }
            )
        } else {
            setDefaultWindowsBackground(window)
            observerUser()
        }
    }

    private fun observerUser() {
        userViewModel.token.observe(this) { event ->
            event.getContentIfNotHandled()?.let(::currentUser)
        }
    }

    override fun onSignInSuccess(result: TokenModel, isRemember: Boolean) {
        userViewModel.setUser(this, result, isRemember)
    }

    override fun onSignUpSuccess(result: TokenModel, isRemember: Boolean) {
        userViewModel.setUser(this, result, isRemember)
    }
}