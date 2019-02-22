package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.model.Token
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import javax.servlet.http.HttpSession


@Service
class RouteServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : RouteService {

    override fun getRoute(routeId: Long): Route {

        val url = "$restServerUrl$ROUTE_MAPPING?route_id=$routeId"

        val routeEntity = restTemplate.getForEntity(url, Route::class.java)
        return routeEntity.body!!
    }

    override fun getRoutes(): Array<Route> {
        val url = "$restServerUrl$ROUTE_MAPPING"

        val routeEntity = restTemplate.getForEntity(url, Array<Route>::class.java)
        return routeEntity.body!!
    }
}