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
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/register")
    fun register(
            session: HttpSession,
            model: Model
    ) : String {

        return "register"
    }

    @GetMapping("/login")
    fun login(
            session: HttpSession,
            model: Model
    ) : String {

        return "login"
    }
}