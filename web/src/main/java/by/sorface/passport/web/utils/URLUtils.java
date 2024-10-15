package by.sorface.passport.web.utils;

import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URL;

@UtilityClass
public class URLUtils {

    public boolean isValidRedirectUrl(String url) {
        try {
            URL redirectUrl = new URL(url);

            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
