package by.devpav.kotlin.oidcidp.utils

object OtpUtils {

    fun generateOTP(length: Int) : String{
        if (length <= 1) {
            throw IllegalArgumentException("OTP length must be greater than 1")
        }

        return (0..length).joinToString(separator = "") { (1 until 10).random().toString() }
    }

}