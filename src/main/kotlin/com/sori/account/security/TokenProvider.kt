package com.sori.account.security

import com.sori.account.config.SecurityProperties
import com.sori.account.service.AppUserDetailsService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Instant
import java.util.*

@Component
class TokenProvider(
    private val securityProperties: SecurityProperties,
    private val userDetailsService: AppUserDetailsService,
) {
    private var key: Key? = null
    private var tokenValidity: Date? = null

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(securityProperties.secret.toByteArray())
        tokenValidity = Date.from(Instant.now().plusSeconds(securityProperties.expirationTime))
    }

    fun createToken(authentication: Authentication): String {
        val authClaims: MutableList<String> = mutableListOf()
        authentication.authorities?.let { authorities ->
            authorities.forEach { claim -> authClaims.add(claim.toString()) }
        }

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("role", authClaims)
            .setExpiration(tokenValidity)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getAuthentication(token: String): Authentication? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace(securityProperties.tokenPrefix, ""))
            val userDetail = userDetailsService.loadUserByUsername(claims.body.subject)
            val principal = User(userDetail.username, "", userDetail.authorities)
            UsernamePasswordAuthenticationToken(principal, token, userDetail.authorities)
        } catch (e: Exception) {
            return null
        }
    }
}