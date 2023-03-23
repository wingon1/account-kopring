package com.sori.account.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sori.account.config.SecurityProperties
import com.sori.account.model.entity.User
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.core.AuthenticationException
import java.io.IOException

class JWTAuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val securityProperties: SecurityProperties,
    private val tokenProvider: TokenProvider
) : UsernamePasswordAuthenticationFilter(){
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse?
    ): Authentication {
        return try {
            val mapper = jacksonObjectMapper()
            val user = mapper
                .readValue<User>(req.inputStream)

            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    user.email,
                    user.password,
                    ArrayList()
                )
            )
        } catch (e: IOException) {
            throw AuthenticationServiceException(e.message)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain?,
        authentication: Authentication
    ) {
        val token = tokenProvider.createToken(authentication)
        // TODO: Response Body에 담아서 보내야 함
        res.addHeader(securityProperties.headerString, securityProperties.tokenPrefix + token)
    }

    // TODO: unsuccessfulAuthentication 구현
}