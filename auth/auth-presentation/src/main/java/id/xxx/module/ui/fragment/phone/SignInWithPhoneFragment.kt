package id.xxx.module.ui.fragment.phone

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import id.xxx.module.auth.presentation.databinding.FragmentSignInWithPhoneBinding
import id.xxx.module.data.Resources
import id.xxx.module.data.model.CountryCode
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth
import id.xxx.module.ui.fragment.phone.adapter.CountryCodeAdapter
import id.xxx.module.ui.fragment.recaptcha.RecaptchaFragment
import id.xxx.module.ui.fragment.recaptcha.RecaptchaFragmentCallback

class SignInWithPhoneFragment : BaseFragmentViewBindingAuth<FragmentSignInWithPhoneBinding>(),
    RecaptchaFragmentCallback {

    companion object {
        private const val KEY_SESSION_INFO = "key_session_info"
    }

    private var _sessionInfo: String? = null

    private val listener by lazy {
        if (activity is SignInWithPhoneListener) {
            activity as SignInWithPhoneListener
        } else if (parentFragment is SignInWithPhoneListener) {
            parentFragment as SignInWithPhoneListener
        } else {
            null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SESSION_INFO, _sessionInfo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childFragmentManager.fragments.isEmpty()) {
                        isEnabled = false
                        activity?.onBackPressed()
                    } else {
                        childFragmentManager.popBackStack()
                    }
                }
            })

        viewBinding.groupConfirm.isVisible =
            savedInstanceState?.getString(KEY_SESSION_INFO) != null

        viewBinding.tvUseEmail.setOnClickListener {
            listener?.onInvoke(SignInWithPhoneCallback.OnClickUseEmail)
        }

        val countryCodes = CountryCode.from(context)
            .map { it.dial_code }
        viewBinding.spinnerCodeArea.adapter = CountryCodeAdapter(
            requireContext(), CountryCode.from(context)
        )
        var codeArea = countryCodes[0]
        var codeAreaOld: CharSequence = codeArea
        viewBinding.etPhone.doOnTextChanged { text, _, _, _ ->
            val input = "$text"
            if (!input.contains(codeArea, true)) {
                viewBinding.etPhone.setText(codeAreaOld)
                viewBinding.etPhone.setSelection(codeAreaOld.length)
            } else {
                codeAreaOld = input
            }
            resetIfError(viewBinding.textInputLayoutPhone)
        }
        viewBinding.etPhone.setText(codeArea)
        viewBinding.spinnerCodeArea.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val tmp = codeArea
                    codeArea = countryCodes[position]
                    val text = "${viewBinding.etPhone.text}".replace(tmp, codeArea)
                    viewBinding.etPhone.setText(text)
                    viewBinding.etPhone.setSelection(text.length)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        viewBinding.btnGetOtp.setOnClickListener {
            if (viewBinding.etPhone.text.isNullOrBlank()) {
                viewBinding.textInputLayoutPhone.error = "Please enter phone number"
            } else {
                if (childFragmentManager.fragments.firstOrNull() !is RecaptchaFragment) {
                    childFragmentManager.beginTransaction()
                        .replace(
                            viewBinding.containerRecaptcha.id,
                            RecaptchaFragment::class.java,
                            null,
                            null
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        viewBinding.btnConfirm.setOnClickListener(::onClickButtonConfirm)
    }

    private fun onClickButtonConfirm(view: View) {
        val sessionInfo = _sessionInfo
        if (sessionInfo.isNullOrBlank()) {
            val message = "You haven't taken OTP, please take OTP first"
            Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
        } else {
            val code = "${viewBinding.etPassword.text}"
            if (code.isEmpty()) {
                viewBinding.textInputLayoutOtp.error = "Please enter OTP"
                viewBinding.etPassword.doOnTextChanged(::onTextOtpChange)
            } else {
                val isRemember = viewBinding.cbRememberMe.isChecked
                listener?.onInvoke(
                    SignInWithPhoneCallback.OnClickConfirm(
                        sessionInfo = sessionInfo,
                        code = code,
                        isRemember = isRemember,
                        rc = { resources ->
                            when (resources) {
                                is Resources.Loading -> {
                                    viewBinding.pb.isVisible = true
                                }
                                is Resources.Success -> {
                                    viewBinding.pb.isVisible = false
                                }
                                is Resources.Failure -> {
                                    viewBinding.pb.isVisible = false
                                    Toast.makeText(
                                        view.context,
                                        resources.value.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                )
            }
        }
    }

    private fun onTextOtpChange(text: CharSequence?, start: Int, before: Int, count: Int) {
        resetIfError(viewBinding.textInputLayoutOtp)
    }

    private fun resetIfError(textInputLayout: TextInputLayout) {
        if (textInputLayout.error != null) textInputLayout.error = null
    }

    override fun onProgressChanged(webView: WebView?, progress: Int) {
        if (progress >= 100) {
            viewBinding.pb.isVisible = false
            val ime = webView?.context?.getSystemService<InputMethodManager>()
            ime?.hideSoftInputFromWindow(webView.windowToken, 0)
        } else {
            viewBinding.pb.isVisible = true
        }
    }

    override fun onSuccess(token: String) {
        val phoneNumber = "${viewBinding.etPhone.text}"
        listener?.onInvoke(
            SignInWithPhoneCallback.OnClickGetOtp(
                phoneNumber = phoneNumber,
                token = token,
                rc = {
                    when (it) {
                        is Resources.Loading -> {
                            viewBinding.pb.isVisible = true
                        }
                        is Resources.Success -> {
                            val sessionInfo = it.value
                            _sessionInfo = sessionInfo
                            viewBinding.groupConfirm.isVisible = true
                            viewBinding.pb.isVisible = false
                        }
                        is Resources.Failure -> {
                            val message = "${it.value.message}\nPlease try again"
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            viewBinding.pb.isVisible = false
                        }
                    }
                }
            )
        )
        childFragmentManager.popBackStack()
    }

    override fun onError() {
        childFragmentManager.popBackStack()
        Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
    }
}