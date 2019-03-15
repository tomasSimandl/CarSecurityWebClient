package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal
import javax.servlet.http.HttpSession

@Controller
class SettingsController(
        private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(SettingsController::class.java)

    @GetMapping("/settings")
    fun userSettingPage(
            principal: Principal,
            session: HttpSession,
            model: Model
    ) : String {

        val user = userService.getUser(principal.name)

        model.addAttribute("user", user)
        return "settings"
    }
}