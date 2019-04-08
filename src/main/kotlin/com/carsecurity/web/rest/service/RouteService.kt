package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Route

/**
 * Service which is used for sending requests to data server.
 */
interface RouteService {

    /**
     * Method send request to data server to get route with given id.
     *
     * @param routeId is identification number of route in database on data server.
     */
    fun getRoute(routeId: Long): Route

    /**
     * Method send request to data server to get events from [page] to [pageLimit] which was created by logged user.
     *
     * @param page number of requested page.
     * @return founded routes.
     */
    fun getRoutes(page: Int): Array<Route>

    /**
     * Method send request to data server to get events from [page] to [pageLimit] which was created by [carId].
     *
     * @param page number of requested page.
     * @param carId is identification number of car in database on data server.
     * @return founded routes.
     */
    fun getRoutesByCar(page: Int, carId: Long): Array<Route>

    /**
     * Method send request to data server to get view of map with route.
     *
     * @param routeId identification number of requested route in database on data server.
     * @return created view of map in [ByteArray].
     */
    fun getRouteMap(routeId: Long): ByteArray

    /**
     * Method send request to data server to get GPX file of route which is specified by [routeId].
     *
     * @param routeId is identification of route in database on data server.
     * @return GPX file as string or empty string on error.
     */
    fun getRouteGPX(routeId: Long): String

    /**
     * Method send request to data server to count all users routes in database.
     * @return number of found routes in database on data server.
     */
    fun countRoutes(): Long

    /**
     * Method send request to data server to count all routes which was created by car with id [carId].
     *
     * @param carId is identification number of car in database on data server.
     * @return number of found routes in database on data server.
     */
    fun countRoutesByCar(carId: Long): Long

    /**
     * Method send request to data server to update routes note.
     *
     * @param id is identification number of route in database on data server.
     * @param note is new note about route.
     */
    fun updateRoute(id: Long, note: String)

    /**
     * Method send request to delete route in database with id [routeId].
     *
     * @param routeId is identification of route in database on data server.
     */
    fun removeRoute(routeId: Long)
}