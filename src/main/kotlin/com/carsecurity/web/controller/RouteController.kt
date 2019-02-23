package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.RouteService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@Controller
class RouteController(private val routeService: RouteService) {

    private val logger = LoggerFactory.getLogger(RouteController::class.java)

    @GetMapping("route/{id}")
    fun getRouteById(@PathVariable("id") id: Long, model: Model): String {

        val route = routeService.getRoute(id)

        model.addAttribute("route", route)
        return "route"
    }

    @GetMapping("route")
    fun getRoutes(model: Model): String {

        val routes = routeService.getRoutes()

        model.addAttribute("routes", routes)
        return "routes"
    }

    @ResponseBody
    @GetMapping(value = ["route/map"], params = ["route_id"], produces = ["image/png"])
    fun getMap(
            @RequestParam("route_id") routeId: Long
    ): ByteArray {
        return routeService.getRouteMap(routeId)
    }
}