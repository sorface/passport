package by.sorface.idp.config.security.backchannel.context

import java.util.*

class OidcLogoutContext private constructor(context: Map<Any, Any?>) : OidcContext {

    private val context: Map<Any, Any?> = Collections.unmodifiableMap(HashMap(context))

    override fun <V : Any?> get(key: Any): V? {
        return if (hasKey(key)) this.context[key] as V?  else null
    }

    override fun hasKey(key: Any?): Boolean {
        return context.containsKey(key!!)
    }

    class Builder : OidcContext.AbstractBuilder<OidcLogoutContext, Builder>() {

        override fun build() : OidcLogoutContext {
            return OidcLogoutContext(getContext());
        }

    }

}