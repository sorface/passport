package by.devpav.kotlin.oidcidp.service.oauth

import by.devpav.kotlin.oidcidp.config.security.oauth2.records.ExternalOAuth2User
import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel

/**
 * Сервис работы с пользовательскими данными из внешних систем (GitHub, Yandex, Twitch и т. д.)
 */
interface SocialOAuth2UserService<T : ExternalOAuth2User> {

    /**
     * Получение/создание пользователя на платформе по данным пользователя из внешних систем
     *
     * @param oAuth2user пользовательские данные из внешней системы
     * @return пользователь полученный/созданные внутри системы
     */
    fun findOrCreate(oAuth2user: T): UserModel

}