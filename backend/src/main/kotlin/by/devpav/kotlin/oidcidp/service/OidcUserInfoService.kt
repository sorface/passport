package by.devpav.kotlin.oidcidp.service

import org.springframework.security.oauth2.core.oidc.OidcUserInfo

/**
 * Сервис для загрузки информации о пользователе в формате OidcUserInfo.
 */
interface OidcUserInfoService {

    /**
     * Загружает информацию о пользователе на основе его имени и набора областей.
     *
     * @param name Имя пользователя.
     * @param scopes Набор областей.
     * @return Информация о пользователе в формате OidcUserInfo.
     */
    fun loadUser(name: String, scopes: Set<String?>): OidcUserInfo

}