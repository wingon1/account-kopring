package com.sori.account.config

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "jwt-security")
@Validated
class SecurityProperties (

    @field:NotBlank
    @field:Size(min = 64)
    var secret: String = "",

    @field:Positive
    var expirationTime: Long = 10800,// in seconds

    @field:Positive
    var strength : Int = 10,

    val tokenPrefix: String = "Bearer ",
    val headerString: String = "Authorization"
)