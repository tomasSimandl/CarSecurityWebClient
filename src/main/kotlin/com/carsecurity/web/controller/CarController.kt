package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.CarService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class CarController(private val carService: CarService) {

    private val logger = LoggerFactory.getLogger(CarController::class.java)

    @GetMapping("car")
    fun getCars(model: Model): String {

        val cars = carService.getCars()

        model.addAttribute("cars", cars)
        return "car"
    }

    @PostMapping("car")
    fun createCar(
            request: HttpServletRequest,
            response: HttpServletResponse,
            @RequestParam(value = "name") carName: String,
            @RequestParam(value = "icon") carIcon: String
    ) {
        response.writer.print(carService.createCar(carName, carIcon))
    }
}