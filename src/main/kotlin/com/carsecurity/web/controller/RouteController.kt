package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.PositionService
import com.carsecurity.web.rest.service.RouteService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam
import kotlin.math.ceil

@Controller
class RouteController(
        private val routeService: RouteService,
        private val positionService: PositionService,

        @Value("\${web.data.load.page.limit}")
        private val pageLimit: Int,

        @Value("\${bing.map.key}")
        private val bingMapKey: String
) {

    private val logger = LoggerFactory.getLogger(RouteController::class.java)

    @GetMapping("route/{id}")
    fun getRouteById(@PathVariable("id") id: Long, model: Model): String {

        val route = routeService.getRoute(id)
        val positions = positionService.getPositions(id)

        model.addAttribute("route", route)
        model.addAttribute("positions", positions)
        model.addAttribute("bingKey", bingMapKey)
        return "route"
    }

    @GetMapping("route")
    fun getRoutes(
            model: Model,
            @RequestParam("page", defaultValue = "1", required = false) page: Int
    ): String {

        val validPage = if(page > 0) page - 1 else 0
        val routes = routeService.getRoutes(validPage)
        val numberOfRoutes = routeService.countRoutes()

        model.addAttribute("routes", routes)
        model.addAttribute("numberOfPages", ceil(numberOfRoutes/pageLimit.toFloat()))
        model.addAttribute("actualPage", validPage + 1)
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