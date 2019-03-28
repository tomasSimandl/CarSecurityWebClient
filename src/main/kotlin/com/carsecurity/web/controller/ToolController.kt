package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.ToolService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 * This controller is used for all requests which are associated activation and deactivation of tools on device.
 *
 * @param toolService is service used for activation and deactivation tools on device.
 */
@Controller
class ToolController(
        private val toolService: ToolService
) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(ToolController::class.java)

    /**
     * This method send request to data server to activate tool od device specified by [carId].
     *
     * @param carId is identification number of car on data server on which will be tool activated.
     * @param tool is name of tool which will be activated.
     * @param model is holder for page attributes.
     */
    @PostMapping("tool/activate")
    @ResponseBody
    fun activateTool(
            model: Model,
            @RequestParam(value = "car_id") carId: Long,
            @RequestParam(value = "tool") tool: String
    ) {
        toolService.activateTool(carId, tool)
    }

    /**
     * This method send request to data server to deactivate tool od device specified by [carId].
     *
     * @param carId is identification number of car on data server on which will be tool deactivated.
     * @param tool is name of tool which will be deactivated.
     * @param model is holder for page attributes.
     */
    @PostMapping("tool/deactivate")
    @ResponseBody
    fun deactivateTool(
            model: Model,
            @RequestParam(value = "car_id") carId: Long,
            @RequestParam(value = "tool") tool: String
    ) {
        toolService.deactivateTool(carId, tool)
    }
}