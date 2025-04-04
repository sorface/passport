package by.sorface.idp.config.security.backchannel.context

import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.context.Context

interface OidcContext : Context {

    fun registeredClient(): RegisteredClient {
        return get(RegisteredClient::class)!!
    }

    fun principalName(): String {
        return get(AbstractBuilder.PRINCIPAL_AUTHENTICATION_PARAM_NAME)!!
    }

    fun oidcIdToken(): String {
        return get(AbstractBuilder.OIDC_ID_TOKEN_PARAM_NAME)!!
    }

    fun sessionId(): String? {
        return get(AbstractBuilder.SESSION_PARAM_NAME)!!
    }

    abstract class AbstractBuilder<T : OidcContext, B : AbstractBuilder<T, B>> {

        companion object {
            const val PRINCIPAL_AUTHENTICATION_PARAM_NAME: String = "principalName"

            const val SESSION_PARAM_NAME: String = "sessionId"

            const val OIDC_ID_TOKEN_PARAM_NAME: String = "oidcIdToken"
        }

        private val context: MutableMap<Any, Any?> = mutableMapOf()

        fun registeredClient(registeredClient: RegisteredClient): B {
            return put(RegisteredClient::class, registeredClient)
        }

        fun oidcIdToken(oidcIdToken: String): B {
            return put(OIDC_ID_TOKEN_PARAM_NAME, oidcIdToken)
        }

        fun sessionId(sessionId: String): B {
            return put(SESSION_PARAM_NAME, sessionId)
        }

        fun principalName(principalName: String): B {
            return put(PRINCIPAL_AUTHENTICATION_PARAM_NAME, principalName)
        }

        private fun put(key: Any, value: Any): B {
            context[key] = value

            return self()
        }

        protected fun getContext(): MutableMap<Any, Any?> = context

        @Suppress("UNCHECKED_CAST")
        private fun <B> self(): B {
            return this as B
        }

        abstract fun build(): T
    }
}