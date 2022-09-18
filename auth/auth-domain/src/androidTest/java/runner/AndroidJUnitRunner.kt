package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class AndroidJUnitRunner : AndroidJUnitRunner() {

    class App : Application()

    override fun newApplication(
        classLoader: ClassLoader?, className: String?, context: Context?
    ): Application {
        return super.newApplication(classLoader, App::class.java.name, context)
    }
}