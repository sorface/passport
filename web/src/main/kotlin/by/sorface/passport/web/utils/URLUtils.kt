package by.sorface.passport.web.utils

import lombok.experimental.UtilityClass
import java.net.MalformedURLException
import java.net.URL

object URLUtils {
    fun isValidRedirectUrl(url: String?): Boolean {
        try {
            val redirectUrl = URL(url)

            return true
        } catch (e: MalformedURLException) {
            return false
        }
    }
}
