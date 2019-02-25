package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Count
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

        @Value("\${web.data.load.page.limit}")
        private val pageLimit: Int,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : RouteService {

    override fun getRoute(routeId: Long): Route {

        val url = "$restServerUrl$ROUTE_MAPPING?route_id=$routeId"

        val routeEntity = restTemplate.getForEntity(url, Route::class.java)
        return routeEntity.body!!
    }

    override fun getRoutes(page: Int): Array<Route> {
        val url = "$restServerUrl$ROUTE_MAPPING?page=$page&limit=$pageLimit"

        val routeEntity = restTemplate.getForEntity(url, Array<Route>::class.java)
        return routeEntity.body!!
    }

    override fun getRouteMap(routeId: Long): ByteArray {
        val url = "$restServerUrl$ROUTE_MAPPING/map?route_id=$routeId"

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_OCTET_STREAM)

        val httpEntity = HttpEntity<String>(headers)

        val entity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ByteArray::class.java)
//        val entity = restTemplate.getForEntity(url, ByteArray::class.java)
        return entity.body ?: ByteArray(0)
    }

    override fun countRoutes(): Long {
        val url = "$restServerUrl$ROUTE_COUNT_MAPPING"

        val routeEntity = restTemplate.getForEntity(url, Count::class.java)
        return routeEntity.body!!.count
    }

}