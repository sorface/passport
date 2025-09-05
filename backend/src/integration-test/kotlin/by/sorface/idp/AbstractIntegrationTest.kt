package by.sorface.idp

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import ru.sorface.boot.security.core.principal.SorfacePrincipal
import java.util.*

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockSorfacePrincipal(
    val id: String = "c764ae76-7aef-41c1-afdc-c093f9db0150",
    val username: String = "sorface",
    val password: String = "password",
    val authorities: Array<String> = [],
)


class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockSorfacePrincipal> {
    override fun createSecurityContext(annotation: WithMockSorfacePrincipal): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val principal = SorfacePrincipal(
            id = UUID.fromString(annotation.id),
            username = annotation.username,
            password = annotation.password,
            authorities = annotation.authorities.map { it }.toSet()
        )

        val auth: Authentication = UsernamePasswordAuthenticationToken(principal, annotation.password, principal.authorities)

        context.authentication = auth

        return context
    }
}

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IdpApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

}
