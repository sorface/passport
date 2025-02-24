package by.devpav.kotlin.oidcidp.records

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class SorfacePrincipal(

    /**
     * user id
     */
    var id: UUID? = null,

    /**
     * user username
     */
    private val username: String,

    /**
     * user password
     */
    private var password: String,

    /**
     * user roles
     */
    private var authorities: MutableSet<String> = mutableSetOf()

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities.map { SimpleGrantedAuthority(it) }.toMutableSet()

    override fun getPassword(): String = password

    override fun getUsername(): String = username

}