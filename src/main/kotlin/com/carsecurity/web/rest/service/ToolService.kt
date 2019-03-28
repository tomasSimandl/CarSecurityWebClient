package com.carsecurity.web.rest.service

/**
 * Service which is used for sending requests to data server.
 */
interface ToolService {

    /**
     * Method send request to data server to deactivate tool on car.
     *
     * @param carId is identification number of car on data server. To this car will be send request to deactivate tool.
     * @param tool is tool which will be deactivated on device.
     * @return response of server as a string.
     */
    fun deactivateTool(carId: Long, tool: String): String?

    /**
     * Method send request to data server to activate tool on car.
     *
     * @param carId is identification number of car on data server. To this car will be send request to activate tool.
     * @param tool is tool which will be activated on device.
     * @return response of server as a string.
     */
    fun activateTool(carId: Long, tool: String): String?
}