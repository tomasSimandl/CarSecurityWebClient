package com.carsecurity.web.controller

import com.carsecurity.web.rest.model.Token
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
class TestController {

    private val logger = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping("test")
    fun test(session: HttpSession, model: Model) : String {

        val token = session.getAttribute("token") as Token

        //model.addAttribute("error", "${token.refreshToken}</br>${token.accessToken}</br>${token.tokenType}</br>${token.scope}</br>${token.expiresIn}")
        return "test"
    }
}