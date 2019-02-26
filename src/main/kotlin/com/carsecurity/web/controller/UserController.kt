package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.security.Principal
import javax.servlet.http.HttpSession

@Controller
class UserController(private val userService: UserService) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

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

    @PostMapping("/register")
    fun registerPost(
            principal: Principal?,
            session: HttpSession,
            model: Model,
            @RequestParam("username") username: String,
            @RequestParam("password") password: String,
            @RequestParam("confirmation") confirmPassword: String
    ) : String {

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "Please fill all inputs!")
            return "register"
        }

        if(password != confirmPassword) {
            model.addAttribute("error", "Passwords do not match!")
            return "register"
        }

        if (password.length < 8) {
            model.addAttribute("error", "Password is too short. Minimum is 8 characters.")
            return "register"
        }

        val responseCode = userService.register(username, password)
        when(responseCode) {
            201 -> return "redirect:/login?registered"
            400 -> {
                model.addAttribute("error", "Invalid input data")
                return "register"
            }
            else -> {
                model.addAttribute("error", "Can not communicate with server. Try again later!")
                return "register"
            }
        }
    }


}