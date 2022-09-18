package id.xxx.started_project.ui.started_activity.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import id.xxx.module.data.model.TokenModel
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth
import id.xxx.module.ui.viewmodel.UserViewModel
import id.xxx.started_project.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : BaseFragmentViewBindingAuth<FragmentHomeBinding>() {

    companion object {
        const val DATA_EXTRA_UID = "DATA EXTRA UID"
    }

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = arguments?.getSerializable(DATA_EXTRA_UID) as TokenModel
        println("expires_in: ${Date(token.expiresIn)}")
        println("uid: ${token.uid}")
        println("token: ${token.token}")

        viewBinding.btnSignOut.setOnClickListener { v ->
            userViewModel.setUser(v.context, null, false)
        }
    }

    fun onUserChange(value: TokenModel) {
        println(value)
    }
}