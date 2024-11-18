package by.sorface.passport.web.utils

import java.net.MalformedURLException
import java.net.URL

object URLUtils {
    fun isValidRedirectUrl(url: String?): Boolean {
        try {
            URL(url)

            return true
        } catch (e: MalformedURLException) {
            return false
        }
    }
}
