package by.devpav.kotlin.oidcidp.config

import by.devpav.kotlin.oidcidp.config.security.constants.JwtClaims.USER_ROLES_CLAIM_NAME
import by.devpav.kotlin.oidcidp.config.security.oauth2.customizers.IdTokenCustomizerConfig
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.jwt.JwtClaimsSet.Builder
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import java.util.function.Consumer

@ExtendWith(MockKExtension::class)
class IdTokenCustomizerConfigTest {

    private var config: IdTokenCustomizerConfig = IdTokenCustomizerConfig()

    @Test
    fun `set token claims (roles)`() {
        val roles = listOf(SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN"));

        val jwtEncodingContext = mockk<JwtEncodingContext>(relaxed = true)
        val oAuth2TokenType = mockk<OAuth2TokenType>()
        val jwtClaims = mockk<Builder>()
        val authentication = mockk<Authentication>()

        every {  oAuth2TokenType.value } returns OidcParameterNames.ID_TOKEN

        every { jwtEncodingContext.tokenType } returns oAuth2TokenType
        every { jwtEncodingContext.claims } returns jwtClaims
        every { jwtEncodingContext.getPrincipal<Authentication>() } returns authentication
        every { authentication.authorities } returns roles
        every { jwtClaims.claims(any()) } returns jwtClaims

        config.tokenCustomizer().customize(jwtEncodingContext)

        val capturingSlot: CapturingSlot<Consumer<Map<String, Any>>> = slot<Consumer<Map<String, Any>>>()

        verify {
            jwtClaims.claims(capture(capturingSlot))
        }

        val captured = capturingSlot.captured

        assertNotNull(captured)

        val claimsMap = mutableMapOf<String, Any>()
        captured.accept(claimsMap)

        val actualRoles = claimsMap[USER_ROLES_CLAIM_NAME] as HashSet<*>

        assertNotNull(actualRoles)
        assertEquals(2, actualRoles.size)

        assertTrue(actualRoles.contains("ROLE_USER"))
        assertTrue(actualRoles.contains("ROLE_ADMIN"))
    }
}