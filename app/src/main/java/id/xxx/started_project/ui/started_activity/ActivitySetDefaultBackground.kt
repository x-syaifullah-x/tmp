@file:JvmName(Activity.CLASS_NAME)

package id.xxx.started_project.ui.started_activity

import android.app.Application
import android.util.TypedValue
import android.view.Window

fun Activity.setDefaultWindowsBackground(window: Window) {
    val typedValue = TypedValue()
    (applicationContext as Application)
        .theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
    window.setBackgroundDrawableResource(typedValue.resourceId)
}