package com.carsecurity.web.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal
import javax.servlet.http.HttpSession

@Controller
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/register")
    fun register(
            principal: Principal?,
            session: HttpSession,
            model: Model
    ) : String {

        if(principal != null) {
            return "forward:/"
        }
        return "register"
    }

    @GetMapping("/login")
    fun login(
            principal: Principal?,
            session: HttpSession,
            model: Model
    ) : String {

        if(principal != null) {
            return "forward:/"
        }
        return "/login"
    }
}