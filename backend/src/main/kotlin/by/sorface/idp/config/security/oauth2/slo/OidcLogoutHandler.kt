package by.sorface.idp.config.security.oauth2.slo

import by.sorface.idp.config.web.properties.SessionCookieProperties
import by.sorface.passport.web.security.oauth2.slo.DelegateLogoutSuccessHandler
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler
import org.springframework.stereotype.Component

@Component
class OidcLogoutHandler(
    oidcWebSessionLogoutHandler: OidcWebSessionLogoutHandler,
    oidcPostRedirectLocationLogoutHandler: OidcPostRedirectLocationLogoutHandler,
    sessionCookieProperties: SessionCookieProperties
) : DelegateLogoutSuccessHandler(CookieClearingLogoutHandler(sessionCookieProperties.name),  oidcWebSessionLogoutHandler, oidcPostRedirectLocationLogoutHandler)