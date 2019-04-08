package com.carsecurity.web.rest.service.Impl

import com.carsecurity.web.rest.model.Count
import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.service.RouteService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Implementation of service which is used for sending requests to data server.
 *
 * @param restServerUrl is url address of data server
 * @param restTemplate is template which can be used for sending authorized requests to data server.
 * @param pageLimit is maximal limit of routes on one page.
 */
@Service
class RouteServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Value("\${web.data.load.page.limit}")
        private val pageLimit: Int,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : RouteService {

    /**
     * Method send request to data server to get route with given id.
     *
     * @param routeId is identification number of route in database on data server.
     */
    override fun getRoute(routeId: Long): Route {

        val url = "$restServerUrl$ROUTE_MAPPING?route_id=$routeId"

        val routeEntity = restTemplate.getForEntity(url, Route::class.java)
        return routeEntity.body!!
    }

    /**
     * Method send request to data server to get events from [page] to [pageLimit] which was created by logged user.
     *
     * @param page number of requested page.
     * @return founded routes.
     */
    override fun getRoutes(page: Int): Array<Route> {
        val url = "$restServerUrl$ROUTE_MAPPING?page=$page&limit=$pageLimit"

        val routeEntity = restTemplate.getForEntity(url, Array<Route>::class.java)
        return routeEntity.body!!
    }

    /**
     * Method send request to data server to get events from [page] to [pageLimit] which was created by [carId].
     *
     * @param page number of requested page.
     * @param carId is identification number of car in database on data server.
     * @return founded routes.
     */
    override fun getRoutesByCar(page: Int, carId: Long): Array<Route> {
        val url = "$restServerUrl$ROUTE_MAPPING?page=$page&limit=$pageLimit&car_id=$carId"

        val routeEntity = restTemplate.getForEntity(url, Array<Route>::class.java)
        return routeEntity.body!!
    }

    /**
     * Method send request to data server to get view of map with route.
     *
     * @param routeId identification number of requested route in database on data server.
     * @return created view of map in [ByteArray].
     */
    override fun getRouteMap(routeId: Long): ByteArray {
        val url = "$restServerUrl$ROUTE_MAPPING/map?route_id=$routeId"

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_OCTET_STREAM)

        val httpEntity = HttpEntity<String>(headers)

        val entity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ByteArray::class.java)
        return entity.body ?: ByteArray(0)
    }

    /**
     * Method send request to data server to get GPX file of route which is specified by [routeId].
     *
     * @param routeId is identification of route in database on data server.
     * @return GPX file as string or empty string on error.
     */
    override fun getRouteGPX(routeId: Long): String {
        val url = "$restServerUrl$ROUTE_MAPPING/export?route_id=$routeId"

        val entity = restTemplate.getForEntity(url, String::class.java)
        return entity.body ?: ""
    }

    /**
     * Method send request to data server to count all users routes in database.
     * @return number of found routes in database on data server.
     */
    override fun countRoutes(): Long {
        val url = "$restServerUrl$ROUTE_COUNT_MAPPING"

        val routeEntity = restTemplate.getForEntity(url, Count::class.java)
        return routeEntity.body!!.count
    }

    /**
     * Method send request to data server to count all routes which was created by car with id [carId].
     *
     * @param carId is identification number of car in database on data server.
     * @return number of found routes in database on data server.
     */
    override fun countRoutesByCar(carId: Long): Long {
        val url = "$restServerUrl$ROUTE_COUNT_MAPPING?car_id=$carId"

        val routeEntity = restTemplate.getForEntity(url, Count::class.java)
        return routeEntity.body!!.count
    }

    /**
     * Method send request to data server to update routes note.
     *
     * @param id is identification number of route in database on data server.
     * @param note is new note about route.
     */
    override fun updateRoute(id: Long, note: String) {
        val url = "$restServerUrl$ROUTE_MAPPING"

        val form = HashMap<String, String>()
        form["id"] = id.toString()
        form["note"] = note

        val httpEntity = HttpEntity(form)
        restTemplate.put(url, httpEntity)
    }

    /**
     * Method send request to delete route in database with id [routeId].
     *
     * @param routeId is identification of route in database on data server.
     */
    override fun removeRoute(routeId: Long) {
        val url = "$restServerUrl$ROUTE_MAPPING?route_id=$routeId"
        restTemplate.delete(url)
    }

}