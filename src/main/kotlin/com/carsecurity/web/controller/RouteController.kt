package com.carsecurity.web.controller

import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.PositionService
import com.carsecurity.web.rest.service.RouteService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import kotlin.math.ceil

/**
 * This controller is used for all requests which are associated with 'route' page.
 *
 * @param routeService is service which is used for access routes on data server.
 * @param positionService is service which is used for access positions on data server.
 * @param carService is service which is used for access cars on data server.
 * @param pageLimit is maximal number of routes in one page.
 * @param bingMapKey is authorization key for access bing map api.
 */
@Controller
class RouteController(
        private val routeService: RouteService,
        private val positionService: PositionService,
        private val carService: CarService,

        @Value("\${web.data.load.page.limit}")
        private val pageLimit: Int,

        @Value("\${bing.map.key}")
        private val bingMapKey: String
) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(RouteController::class.java)

    /**
     * Method load route given by path parameter and all its positions over [routeService] and [positionService].
     * All loaded data are append to [model]. Result is rendered on 'route' page.
     *
     * @param id is identification number of route in database on data server.
     * @param model is holder for page attributes.
     * @return string "route" which is render to route page.
     */
    @GetMapping("route/{id}")
    fun getRouteById(
            @PathVariable("id") id: Long,
            model: Model
    ): String {

        val route = routeService.getRoute(id)
        val positions = positionService.getPositions(id)

        model.addAttribute("route", route)
        model.addAttribute("positions", positions)
        model.addAttribute("bingKey", bingMapKey)
        return "route"
    }

    /**
     * Method load routes form data server over [routeService] and all users cars over [carService]. Loaded data are
     * append to model. Result is render on 'routes' page.
     *
     * @param model is holder for page attributes.
     * @param page is requested page of events (Optional).
     * @param carId optional parameter which specified that routes will be only of this car.
     */
    @GetMapping("route")
    fun getRoutes(
            model: Model,
            @RequestParam("page", defaultValue = "1", required = false) page: Int,
            @RequestParam("car_id", required = false) carId: Long?
    ): String {

        val validPage = if (page > 0) page - 1 else 0
        val routes: Array<Route>
        val numberOfRoutes: Long

        if (carId == null) {
            routes = routeService.getRoutes(validPage)
            numberOfRoutes = routeService.countRoutes()
        } else {
            model.addAttribute("carId", carId)
            routes = routeService.getRoutesByCar(validPage, carId)
            numberOfRoutes = routeService.countRoutesByCar(carId)
        }
        val cars = carService.getCars()

        model.addAttribute("routes", routes)
        model.addAttribute("cars", cars)
        model.addAttribute("numberOfPages", ceil(numberOfRoutes / pageLimit.toFloat()))
        model.addAttribute("actualPage", validPage + 1)
        return "routes"
    }

    /**
     * Method send request to get view of maps route over [routeService] and return result.
     *
     * @param routeId is identification number of requested route in database on data server.
     * @return map view of route as [ByteArray].
     */
    @ResponseBody
    @GetMapping(value = ["route/map"], params = ["route_id"], produces = ["image/png"])
    fun getMap(
            @RequestParam("route_id") routeId: Long
    ): ByteArray {
        return routeService.getRouteMap(routeId)
    }

    /**
     * Method send request to get route in GPX file over [routeService] and return result.
     *
     * @param routeId is identification number of requested route in database on data server.
     * @return string in GPX format with requested route or empty string on error.
     */
    @ResponseBody
    @GetMapping(value = ["route/export"], params = ["route_id"], produces = ["application/xml"])
    fun getGPXRoute(
            @RequestParam("route_id") routeId: Long
    ): String {
        return routeService.getRouteGPX(routeId)
    }

    /**
     * Method send request to data server over [routeService] to update routes note in database.
     *
     * @param routeId identification number of route in database on data server which will be updated.
     * @param note is new note which will be set to route.
     */
    @ResponseBody
    @PutMapping("route")
    fun updateRoute(
            @RequestParam(value = "id") routeId: Long,
            @RequestParam(value = "note") note: String
    ) {
        routeService.updateRoute(routeId, note)
    }

    /**
     * Method send request to data server to delete route specified by [routeId].
     *
     * @param routeId is identification of route which will be deleted on data server.
     */
    @ResponseBody
    @DeleteMapping("route")
    fun deleteRoute(
            @RequestParam(value = "route_id") routeId: Long
    ) {
        routeService.removeRoute(routeId)
    }
}