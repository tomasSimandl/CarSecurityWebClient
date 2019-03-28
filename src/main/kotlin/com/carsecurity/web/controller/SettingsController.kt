package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

/**
 * This controller is used for all requests which are associated with 'settings' page.
 *
 * @param userService is service which is used for access users on authorization server.
 */
@Controller
class SettingsController(
        private val userService: UserService
) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(SettingsController::class.java)

    /**
     * Method load logged users data from authorization server over [userService]. Load user is append to [model].
     * Result is render over 'settings' page.
     *
     * @param model is holder for page attributes.
     * @param principal is principal of actual logged user.
     * @return string "settings" which is render to settings page.
     */
    @GetMapping("/settings")
    fun userSettingPage(
            principal: Principal,
            model: Model
    ): String {

        val user = userService.getUser(principal.name)

        model.addAttribute("user", user)
        return "settings"
    }
}