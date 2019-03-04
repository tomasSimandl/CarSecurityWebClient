package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.StatusService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal
import javax.servlet.http.HttpSession

@Controller
class HomeController(
        private val statusService: StatusService,
        private val carService: CarService
) {

    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    @GetMapping("/")
    fun statusPage(
            principal: Principal?,
            session: HttpSession,
            model: Model
    ) : String {

        val cars = carService.getCars()

        model.addAttribute("cars", cars)
        return "status"
    }


}