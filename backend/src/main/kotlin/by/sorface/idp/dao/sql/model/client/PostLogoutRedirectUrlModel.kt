package by.sorface.idp.dao.sql.model.client

import jakarta.persistence.*

@Entity
@DiscriminatorValue("POST_LOGOUT_REDIRECT_URL")
class PostLogoutRedirectUrlModel : RedirectUrlModel()
