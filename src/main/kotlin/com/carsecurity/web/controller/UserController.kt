package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.LoginService
import com.carsecurity.web.rest.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.security.Principal

/**
 * This controller is used for all requests which are associated with operation with user, login and registration.
 *
 * @param loginService is service which is used for login and register users on authorization server.
 * @param userService is service which is used for access users on authorization server.
 * @param carService is service which is used for access cars on data server.
 */
@Controller
class UserController(
        private val loginService: LoginService,
        private val userService: UserService,
        private val carService: CarService
) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    /**
     * This method show login page when user is not logged. When is already login, is forward to home page.
     *
     * @param principal of actual login user.
     * @return string "login" which identifies render of login page or 'forward:/' to forward to home page.
     */
    @GetMapping("/login")
    fun login(
            principal: Principal?
    ): String {

        if (principal != null) {
            return "forward:/"
        }
        return "login"
    }

    /**
     * This method show register page when user is not logged. When is already login, is forward to home page.
     *
     * @param principal of actual login user.
     * @return string "register" which identifies render of register page or 'forward:/' to forward to home page.
     */
    @GetMapping("/register")
    fun register(
            principal: Principal?
    ): String {

        if (principal != null) {
            return "forward:/"
        }
        return "register"
    }

    /**
     * Method check all input parameters and send register request to authorization server over [loginService].
     * Errors are append to model and show on register page. On success is redirect to login page.
     *
     * @param model is holder for page attributes.
     * @param username of user which will be registered.
     * @param password of user which will be registered.
     * @param confirmPassword confirmation of [password].
     * @return render of registration page or login page.
     */
    @PostMapping("/register")
    fun registerPost(
            model: Model,
            @RequestParam("username") username: String,
            @RequestParam("password") password: String,
            @RequestParam("confirmation") confirmPassword: String
    ): String {

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "Please fill all inputs!")
            return "register"
        }

        if (password != confirmPassword) {
            model.addAttribute("error", "Passwords do not match!")
            return "register"
        }

        if (password.length < 8) {
            model.addAttribute("error", "Password is too short. Minimum is 8 characters.")
            return "register"
        }

        val responseCode = loginService.register(username, password)
        when (responseCode) {
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

    /**
     * Method sends request to authorization server over [userService] to update users email.
     *
     * @param userId is identification number of user in database on authorization server.
     * @param email is new email which will be store to user on authorization server.
     */
    @PutMapping("/user")
    @ResponseBody
    fun updateEmail(
            @RequestParam("id") userId: Long,
            @RequestParam("email") email: String
    ) {
        userService.updateUserEmail(userId, email)
    }

    /**
     * Method send requests to remove users cars on data server over [carService] and to remove user on
     * authorization server over [userService].
     *
     * @param userId is identification number of user which should be removed from database.
     */
    @DeleteMapping("/user")
    @ResponseBody
    fun deleteUser(
            @RequestParam(value = "id") userId: Long
    ) {
        carService.removeCars()
        userService.removeUser(userId)
    }
}