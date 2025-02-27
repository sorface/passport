package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.web.rest.model.accounts.AccountRegistration
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.AccountRegistrationResult

interface AccountRegistryFacade {

    fun registry(account: AccountRegistration): AccountRegistrationResult

}