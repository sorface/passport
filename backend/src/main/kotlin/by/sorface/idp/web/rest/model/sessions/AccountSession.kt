package by.sorface.idp.web.rest.model.sessions

/**
 * Класс, представляющий сессию аккаунта.
 *
 * @property id Идентификатор сессии.
 * @property device Устройство, с которого была создана сессия.
 * @property deviceBrand Бренд устройства.
 * @property deviceType Тип устройства.
 * @property browser Браузер, который использовался для создания сессии.
 * @property createdAt Время создания сессии.
 * @property active Флаг, указывающий, активна ли сессия.
 */
class AccountSession {
    var id: String? = null

    var device: String? = null

    var deviceBrand: String? = null

    var deviceType: String? = null

    var browser: String? = null

    var createdAt: Long? = null

    var active = false
}
