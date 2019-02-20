package com.carsecurity.web.controller

import com.carsecurity.web.rest.model.Token
import com.carsecurity.web.rest.service.RouteService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpStatusCodeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
class RouteController(private val routeService: RouteService) {

    private val logger = LoggerFactory.getLogger(RouteController::class.java)

    @GetMapping("route/{id}")
    fun test(@PathVariable("id") id: Long, session: HttpSession, request: HttpServletRequest, response: HttpServletResponse) : String {

        try {
            val route = routeService.getRoute(id)

            return "${route.id}</br>" +
                    "${route.carId}</br>" +
                    "${route.length}</br>" +
                    "${route.note}</br>" +
                    "${route.startPositionId}</br>" +
                    "${route.endPositionId}</br>"

        } catch (e: HttpStatusCodeException) {
            logger.debug("Can get route. Server response status code: ${e.statusCode} $")

            if (e.statusCode == HttpStatus.UNAUTHORIZED ||
                    e.statusCode == HttpStatus.BAD_REQUEST ||
                    e.statusCode == HttpStatus.FORBIDDEN) {
                return "Not authorized to view this data"
            } else {
                return "Can not get data from server. Http status: ${e.statusCode}"
            }
        } catch (e: Exception) {
            logger.error("Can not login cause exception", e)
            return "Can not get data from server"
        }
    }
}