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

    override fun activateTool(carId: Long, tool: String): String? {
        val url = "$restServerUrl$TOOL_ACTIVATE_MAPPING"
        return switchTool(carId, tool, url)
    }

    override fun deactivateTool(carId: Long, tool: String): String? {
        val url = "$restServerUrl$TOOL_DEACTIVATE_MAPPING"
        return switchTool(carId, tool, url)
    }

    private fun switchTool(carId: Long, tool: String, url: String): String?{
        val map = LinkedMultiValueMap<String, String>()
        map["car_id"] = carId.toString()
        map["tool"] = tool

        val resultEntity = restTemplate.postForEntity(url, map, String::class.java)
        return resultEntity.body
    }
}