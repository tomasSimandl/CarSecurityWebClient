package com.carsecurity.web.controller

import com.carsecurity.web.rest.model.Token
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
class TestController {

    private val logger = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping("test")
    fun test(session: HttpSession, request: HttpServletRequest, response: HttpServletResponse) : String {

        val token = session.getAttribute("token") as Token

        return "${token.refreshToken}</br>${token.accessToken}</br>${token.tokenType}</br>${token.scope}</br>${token.expiresIn}</br></br>TEST BITCH"
    }
}