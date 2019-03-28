package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.StatusService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * This controller is used for display status home page where is status of all connected devices.
 *
 * @param carService is service which is used for access cars on data server.
 * @param statusService is service which is used for access status over data server.
 */
@Controller
class HomeController(
        private val carService: CarService,
        private val statusService: StatusService
) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    /**
     * Method load all users car from data server over [carService] and display them on status page.
     *
     * @param model is holder for page attributes.
     * @return string "status" which is rendered to status page.
     */
    @GetMapping("/")
    fun statusPage(
            model: Model
    ): String {

        val cars = carService.getCars()

        model.addAttribute("cars", cars)
        return "status"
    }

    /**
     * Method send request to data server over [statusService] to get status of specific device. Result is rendered
     * with fragment 'statusFragment' which return div with returned status.
     *
     * @param model is holder for page attribute.
     * @param carId is identification number of car of which status is requested.
     * @return string which represents fragment of status which is render to div with status.
     */
    @GetMapping("status")
    fun getStatus(
            model: Model,
            @RequestParam(value = "car_id") carId: Long
    ): String {

        val status = statusService.getStatus(carId)

        model.addAttribute("status", status)
        return "fragments/status :: statusFragment"
    }
}