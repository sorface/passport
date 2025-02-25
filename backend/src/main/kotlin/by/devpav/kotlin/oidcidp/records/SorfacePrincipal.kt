package by.devpav.kotlin.oidcidp.records

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable
import java.util.*

class SorfacePrincipal(): UserDetails, OAuth2User, Serializable {

    /**
     * user id
     */
    var id: UUID? = null

    /**
     * user username
     */
    private var username: String? = null

    /**
     * user password
     */
    private var password: String? = null

    /**
     * user roles
     */
    private var authorities: List<SimpleGrantedAuthority> = mutableListOf()

    constructor(id: UUID? = null, username: String, password: String? = null, authorities: Set<String> = mutableSetOf()) : this() {
        this.id = id
        this.username = username
        this.password = password
        this.authorities = authorities.map { SimpleGrantedAuthority(it) }.toMutableList()
    }

    override fun getName(): String = username!!

    override fun getAttributes(): MutableMap<String, Any> = HashMap()

    override fun getAuthorities(): List<GrantedAuthority> = authorities.toMutableList()

    override fun getPassword(): String? = password

    override fun getUsername(): String = username!!

}