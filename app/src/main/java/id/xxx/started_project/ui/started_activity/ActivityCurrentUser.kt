@file:JvmName(Activity.CLASS_NAME)

package id.xxx.started_project.ui.started_activity

import android.animation.ObjectAnimator
import android.graphics.drawable.LayerDrawable
import androidx.core.os.bundleOf
import id.xxx.module.data.Resources
import id.xxx.module.data.model.TokenModel
import id.xxx.module.ui.fragment.sign.SignFragment
import id.xxx.started_project.R
import id.xxx.started_project.ui.started_activity.home.HomeFragment

fun Activity.currentUser(resources: Resources<TokenModel>) {
    when (resources) {
        is Resources.Loading -> {
            val windowsBackground = window.decorView.background
            if (windowsBackground is LayerDrawable) {
                val text = windowsBackground.findDrawableByLayerId(R.id.text)
                text.alpha = 0
                val progress = windowsBackground.findDrawableByLayerId(R.id.progress)
                progress.alpha = 255
                val anim = ObjectAnimator.ofInt(
                    progress, "level", 0, 4000
                )
                anim.repeatCount = ObjectAnimator.INFINITE
                anim.start()
            }
        }
        is Resources.Success -> {
            setDefaultWindowsBackground(window)
            val homeFragmentTag = "HomeFragmentTag"
            val fragment = supportFragmentManager.fragments.lastOrNull()
            if (fragment?.tag != homeFragmentTag) {
                val args = bundleOf(HomeFragment.DATA_EXTRA_UID to resources.value)
                supportFragmentManager.beginTransaction()
                    .replace(container, HomeFragment::class.java, args, homeFragmentTag)
                    .commitNow()
            } else {
                (fragment as? HomeFragment)?.onUserChange(resources.value)
            }
        }
        is Resources.Failure -> {
            setDefaultWindowsBackground(window)
            val signFragmentTag = "SignFragmentTag"
            val fragment = supportFragmentManager.fragments.lastOrNull()
            if (fragment?.tag != signFragmentTag) {
                supportFragmentManager.beginTransaction()
                    .replace(container, SignFragment::class.java, null, signFragmentTag)
                    .commitNow()
            }
        }
    }
}
