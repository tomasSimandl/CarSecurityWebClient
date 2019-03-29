package com.carsecurity.web.rest.service.Impl

import com.carsecurity.web.rest.service.ToolService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * Implementation of service which is used for sending requests to data server.
 *
 * @param restServerUrl is url address of data server
 * @param restTemplate is template which can be used for sending authorized requests to data server.
 */
@Service
class ToolServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : ToolService {

    /**
     * Method send request to data server to activate tool on car.
     *
     * @param carId is identification number of car on data server. To this car will be send request to activate tool.
     * @param tool is tool which will be activated on device.
     * @return response of server as a string.
     */
    override fun activateTool(carId: Long, tool: String): String? {
        val url = "$restServerUrl$TOOL_ACTIVATE_MAPPING"
        return switchTool(carId, tool, url)
    }

    /**
     * Method send request to data server to deactivate tool on car.
     *
     * @param carId is identification number of car on data server. To this car will be send request to deactivate tool.
     * @param tool is tool which will be deactivated on device.
     * @return response of server as a string.
     */
    override fun deactivateTool(carId: Long, tool: String): String? {
        val url = "$restServerUrl$TOOL_DEACTIVATE_MAPPING"
        return switchTool(carId, tool, url)
    }

    /**
     * Method send request to activate or deactivate tool on data server.
     *
     * @param carId is identification number of car on data server. To this car will be send request to activate
     *              or deactivate tool.
     * @param tool is tool which will be activated/deactivated on device.
     * @param url url endpoint which decide if it is activation or deactivation command.
     * @return response of server as a string.
     */
    private fun switchTool(carId: Long, tool: String, url: String): String? {
        val map = LinkedMultiValueMap<String, String>()
        map["car_id"] = carId.toString()
        map["tool"] = tool

        val resultEntity = restTemplate.postForEntity(url, map, String::class.java)
        return resultEntity.body
    }
}