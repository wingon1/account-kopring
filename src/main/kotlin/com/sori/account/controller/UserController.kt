package com.sori.account.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {

    @GetMapping(value = ["", "/", "/test"])
    fun allTest() = "OK"

    @GetMapping(value = ["/admin-test"])
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun adminTest() = "OK"

    // TODO: 회원가입

}