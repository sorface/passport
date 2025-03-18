package by.sorface.idp

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName(value = "account controller")
class AccountControllerIntegrationTest : AbstractIntegrationTest() {

    companion object {
        const val ACCOUNT_AUTHENTICATED_REST_URL = "/api/accounts/authenticated"
        const val ACCOUNT_CURRENT_REST_URL = "/api/accounts/current"
    }

    @Test
    @WithMockSorfacePrincipal(authorities = ["USER"])
    fun `get status 'account is authenticated'`() {
        val request = MockMvcRequestBuilders.get(ACCOUNT_AUTHENTICATED_REST_URL)

        mockMvc.perform(request)
            .andExpect(jsonPath("$.access").value(true))
            .andExpect(status().isOk())
    }

    @Test
    @WithAnonymousUser
    fun `get status 'account is not authenticated'`() {
        val request = MockMvcRequestBuilders.get(ACCOUNT_AUTHENTICATED_REST_URL)

        mockMvc.perform(request)
            .andExpect(jsonPath("$.access").value(false))
            .andExpect(status().isOk())
    }

    @Test
    @WithMockSorfacePrincipal(username = "sorface", authorities = ["USER"])
    fun `get current account`() {
        val request = MockMvcRequestBuilders.get(ACCOUNT_CURRENT_REST_URL)

        mockMvc.perform(request)
            .andDo { MockMvcResultHandlers.print() }
            .andExpect { jsonPath("$.id").value { "c764ae76-7aef-41c1-afdc-c093f9db0150" } }
            .andExpect { jsonPath("$.nickname").value { "sorface" } }
            .andExpect { jsonPath("$.email").value { "sorface@sorface.com" } }
            .andExpect { jsonPath("$.firstName").value { "John" } }
            .andExpect { jsonPath("$.lastName").value { "Doe" } }
            .andExpect { jsonPath("$.middleName").value { "Smith" } }
            .andExpect { jsonPath("$.avatar").doesNotExist() }
            .andExpect { jsonPath("$.roles").isArray }
            .andExpect(status().isOk())
    }
}