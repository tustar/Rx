package rx.tustar.com.rxjava

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tustar.rxjava.util.Logger
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OkHttpWrapperTest {

    @Test
    fun http() {
        com.tustar.rxjava.util.http {
            url = "http:www.163.com"
            method = "get"
            onSuccess {
                Logger.d("$it")
            }
            onFail {
                Logger.d("${it.message}")
            }
        }
    }

}