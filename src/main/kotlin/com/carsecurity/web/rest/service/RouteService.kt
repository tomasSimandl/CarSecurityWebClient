package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Route


interface RouteService {

    fun getRoute(routeId: Long): Route
    fun getRoutes(page: Int): Array<Route>
    fun getRoutesByCar(page: Int, carId: Long): Array<Route>
    fun getRouteMap(routeId: Long): ByteArray
    fun countRoutes(): Long
    fun countRoutesByCar(carId: Long): Long
}