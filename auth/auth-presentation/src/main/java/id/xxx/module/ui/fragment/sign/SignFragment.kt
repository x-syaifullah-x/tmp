package id.xxx.module.ui.fragment.sign

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.presentation.databinding.FragmentSignBinding
import id.xxx.module.data.Action
import id.xxx.module.data.Resources
import id.xxx.module.data.model.SignUpFormModel
import id.xxx.module.data.type.SignInType
import id.xxx.module.receiver.AuthActionBroadcastReceiver
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth
import id.xxx.module.ui.fragment.new_password.NewPasswordFragment
import id.xxx.module.ui.fragment.new_password.NewPasswordFragmentCallback
import id.xxx.module.ui.fragment.new_password.NewPasswordFragmentListener
import id.xxx.module.ui.fragment.phone.SignInWithPhoneCallback
import id.xxx.module.ui.fragment.phone.SignInWithPhoneFragment
import id.xxx.module.ui.fragment.phone.SignInWithPhoneListener
import id.xxx.module.ui.fragment.request_otp.RequestOtpFragment
import id.xxx.module.ui.fragment.request_otp.RequestOtpFragmentCallback
import id.xxx.module.ui.fragment.request_otp.RequestOtpFragmentListener
import id.xxx.module.ui.fragment.sign_in.SignInFragment
import id.xxx.module.ui.fragment.sign_in.SignInFragmentCallback
import id.xxx.module.ui.fragment.sign_in.SignInFragmentListener
import id.xxx.module.ui.fragment.sign_up.SignUpFragment
import id.xxx.module.ui.fragment.sign_up.SignUpFragmentCallback
import id.xxx.module.ui.fragment.sign_up.SignUpFragmentListener
import id.xxx.module.ui.viewmodel.SignViewModel
import id.xxx.module.utils.AuthValidation
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignFragment : BaseFragmentViewBindingAuth<FragmentSignBinding>(),
    SignUpFragmentListener,
    SignInFragmentListener,
    NewPasswordFragmentListener,
    RequestOtpFragmentListener,
    SignInWithPhoneListener {

    companion object {
        const val EXTRA_ACTION_MODE = "SIGN_FRAGMENT_EXTRA_ACTION_MODE"

        fun registerReceiverAuthAction(context: Context?, onReceive: (Action) -> Unit) =
            AuthActionBroadcastReceiver.register(context, onReceive)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            childFragmentManager.popBackStack()
        }
    }

    private val signViewModel by viewModel<SignViewModel>()

    private val containerId by lazy { viewBinding.container.id }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(containerId, SignInFragment())
                .commit()

            when (val action = activity?.intent?.extras?.get(EXTRA_ACTION_MODE)) {
                is Action.Mode.ResetPassword -> childFragmentManager.beginTransaction().apply {
                    activity?.intent?.removeExtra(EXTRA_ACTION_MODE)
                    val args = bundleOf(NewPasswordFragment.DATA_EXTRA_OOB_CODE to action.oobCode)
                    add(containerId, NewPasswordFragment::class.java, args)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    override fun onInvoke(rc: SignUpFragmentCallback) {
        when (rc) {
            is SignUpFragmentCallback.OnClickButtonSignUp -> {
                val binding = rc.binding
                val isRemember = binding.cbRememberMe.isChecked
                lifecycleScope.launch {
                    val form = SignUpFormModel(
                        email = "${binding.etEmail.text}",
                        password = "${binding.etPassword.text}"
                    )
                    signViewModel.signUp(form).collect { resources ->
                        binding.btnSignUp.isVisible = resources !is Resources.Loading
                        binding.pb.isVisible = resources is Resources.Loading
                        when (resources) {
                            is Resources.Success ->
                                getListener()?.onSignUpSuccess(resources.value, isRemember)
                            is Resources.Failure ->
                                Toast.makeText(
                                    binding.root.context,
                                    resources.value.message,
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onInvoke(callback: SignInFragmentCallback) {
        when (callback) {
            is SignInFragmentCallback.OnClickSignInWithPhone -> {
                childFragmentManager
                    .beginTransaction().add(containerId, SignInWithPhoneFragment())
                    .addToBackStack(null)
                    .commit()
            }
            is SignInFragmentCallback.OnClickForgetPassword -> {
                childFragmentManager
                    .beginTransaction()
                    .add(containerId, RequestOtpFragment())
                    .addToBackStack(null)
                    .commit()
            }
            is SignInFragmentCallback.OnClickSignIn -> {
                val binding = callback.viewBiding
                val etEmail = binding.etEmail
                val etPassword = binding.etPassword

                if (!AuthValidation.email(etEmail)) return
                else if (!AuthValidation.password(etPassword)) return

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                val btnSignIn = binding.btnSignIn
                val isRemember = binding.cbRememberMe.isChecked

                lifecycleScope.launch {
                    signViewModel.signIn(SignInType.Password(email, password))
                        .collect { resources ->
                            when (resources) {
                                is Resources.Loading -> {
                                    btnSignIn.isEnabled = false
                                }
                                is Resources.Success -> {
                                    btnSignIn.isEnabled = true
                                    getListener()?.onSignInSuccess(resources.value, isRemember)
                                }
                                is Resources.Failure -> {
                                    btnSignIn.isEnabled = true
                                    Toast.makeText(
                                        btnSignIn.context,
                                        resources.value.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                }
            }
            is SignInFragmentCallback.OnClickSignUp -> {
                childFragmentManager.beginTransaction().add(containerId, SignUpFragment())
                    .addToBackStack(null).commit()
            }
        }
    }

    override fun onInvoke(callback: NewPasswordFragmentCallback) {
        if (callback is NewPasswordFragmentCallback.OnClickConfirmNewPassword) {
            val binding = callback.viewBinding
            onClickConfirmNewPassword(
                binding.btnConfirm,
                callback.oobCode,
                callback.newPassword
            )
        }
    }

    private fun onClickConfirmNewPassword(view: View, oobCode: String, newPassword: String) {
        view.isEnabled = false
        lifecycleScope.launch {
            signViewModel.resetPassword(oobCode, newPassword).collect {
                var message: String? = null
                when (it) {
                    is Resources.Success -> {
                        view.isEnabled = true
                        message = "Success change password"
                        childFragmentManager.beginTransaction()
                            .replace(containerId, SignInFragment())
                            .commit()
                    }
                    is Resources.Failure -> {
                        view.isEnabled = true
                        message = it.value.localizedMessage
                    }
                }
                if (message != null) {
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onInvoke(callback: RequestOtpFragmentCallback) {
        when (callback) {
            is RequestOtpFragmentCallback.OnClickBtnGetOtp -> onClickBtnGetOtp(callback)
        }
    }

    private fun onClickBtnGetOtp(callback: RequestOtpFragmentCallback.OnClickBtnGetOtp) {
        lifecycleScope.launch {
            signViewModel.sendOobCode(callback.email).collect {
                val binding = callback.viewBinding
                val btnGetOtp = binding.btnGetOtp
                var message: String? = null
                when (it) {
                    is Resources.Loading -> {
                        btnGetOtp.isEnabled = false
                    }
                    is Resources.Success -> {
                        message =
                            "Verification code sent to your email, please check your email, usually in spam, Thanks"
                        requireActivity().onBackPressed()
                    }
                    is Resources.Failure -> {
                        btnGetOtp.isEnabled = true
                        message = it.value.localizedMessage
                    }
                }
                if (message != null)
                    Toast.makeText(btnGetOtp.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getListener() =
        if (parentFragment is SignFragmentListener)
            parentFragment as SignFragmentListener
        else if (activity is SignFragmentListener)
            activity as SignFragmentListener
        else
            null

    override fun onInvoke(cb: SignInWithPhoneCallback) {
        when (cb) {
            is SignInWithPhoneCallback.OnClickGetOtp -> {
                lifecycleScope.launch {
                    signViewModel.sendVerificationCode(cb.phoneNumber, cb.token)
                        .collect { cb.rc.invoke(it) }
                }
            }
            is SignInWithPhoneCallback.OnClickUseEmail -> {
                childFragmentManager.popBackStack()
            }
            is SignInWithPhoneCallback.OnClickConfirm -> {
                lifecycleScope.launch {
                    signViewModel.signIn(SignInType.Phone(cb.sessionInfo, cb.code))
                        .collect {
                            cb.rc.invoke(it)
                            if (it is Resources.Success) {
                                getListener()?.onSignInSuccess(it.value, cb.isRemember)
                            }
                        }
                }
            }
        }
    }
}