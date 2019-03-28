package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.CarService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This controller is used for all requests which are associated with 'car' page.
 *
 * @param carService is service which is used for access cars on data server.
 */
@Controller
class CarController(private val carService: CarService) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(CarController::class.java)

    /**
     * Method return car page. To car page is added list of all users cars.
     *
     * @param model is holder for page attributes.
     * @return string "car" which is render to car page.
     */
    @GetMapping("car")
    fun getCars(model: Model): String {

        val cars = carService.getCars()

        model.addAttribute("cars", cars)
        return "car"
    }

    /**
     * Method send request to data server over [CarService] and return response to create new car.
     *
     * @param request to create new car on data server.
     * @param response on create new car request.
     * @param carName name of new car.
     * @param carNote note about new car.
     */
    @PostMapping("car")
    fun createCar(
            request: HttpServletRequest,
            response: HttpServletResponse,
            @RequestParam(value = "name") carName: String,
            @RequestParam(value = "note") carNote: String
    ) {
        response.writer.print(carService.createCar(carName, carNote))
    }

    /**
     * Method send request to data server over [CarService] to update car specified by input params.
     *
     * @param carId is identification number of car in database on data server.
     * @param carName is new name of car.
     * @param carNote is new note about car.
     */
    @PutMapping("car")
    fun updateCar(
            @RequestParam(value = "id") carId: Long,
            @RequestParam(value = "name") carName: String,
            @RequestParam(value = "note") carNote: String
    ) {
        carService.updateCar(carId, carName, carNote)
    }

    /**
     * Method send request to data server over [CarService] to remove car from database on data server.
     * @param carId is identification of car in database on data server.
     */
    @DeleteMapping("car")
    fun deleteCar(
            @RequestParam(value = "car_id") carId: Long
    ) {
        carService.removeCar(carId)
    }
}