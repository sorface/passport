package by.devpav.kotlin.oidcidp.config.csrf.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Класс, представляющий свойства CSRF-защиты
 *
 * @property domain Домен, для которого будет установлен cookie
 * @property path Путь, для которого будет установлен cookie
 * @property name Имя cookie
 * @property httpOnly Флаг, указывающий, доступен ли cookie только для HTTP
 * @property secure Флаг, указывающий, должен ли cookie быть установлен только по HTTPS
 */
@ConfigurationProperties("idp.csrf.cookie")
class CsrfCookieProperties {

    var domain: String? = null

    var path: String? = null

    var name: String? = null

    var httpOnly = false

    var secure: Boolean = true

}