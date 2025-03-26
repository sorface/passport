package by.sorface.idp.dao.sql.model.client

import jakarta.persistence.*

/**
 * Класс модели перенаправления клиента.
 * Этот класс представляет таблицу "T_CLIENTREDIRECTURLSTORE" в базе данных.
 */
@Entity
@DiscriminatorValue("REDIRECT_URL")
class ClientRedirectUrlModel : RedirectUrlModel()