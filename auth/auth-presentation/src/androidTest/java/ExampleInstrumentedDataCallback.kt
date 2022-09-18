import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test

class ExampleInstrumentedDataCallback {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertNotNull(appContext)
    }
}