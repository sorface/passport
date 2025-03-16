package by.sorface.idp.service.oauth

import by.sorface.idp.config.security.oauth2.records.ExternalOAuth2User
import by.sorface.idp.dao.sql.model.UserModel

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