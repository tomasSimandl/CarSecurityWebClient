package com.carsecurity.web.rest.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate


@Service
class ToolServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : ToolService {

    override fun activateTool(carId: Long): String {
        val url = "$restServerUrl$TOOL_ACTIVATE_MAPPING"
        return switchTool(carId, url)
    }

    override fun deactivateTool(carId: Long): String {
        val url = "$restServerUrl$TOOL_DEACTIVATE_MAPPING"
        return switchTool(carId, url)
    }

    private fun switchTool(carId: Long, url: String): String {
        val map = LinkedMultiValueMap<String, String>()
        map["car_id"] = carId.toString()

        val resultEntity = restTemplate.postForEntity(url, map, String::class.java)
        return resultEntity.body!!
    }
}