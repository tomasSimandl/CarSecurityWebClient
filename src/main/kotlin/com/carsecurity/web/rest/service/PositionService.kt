package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Position


/**
 * Service which is used for sending requests to data server.
 */
interface PositionService {

    /**
     * Method send request to data server to get all positions of given route.
     *
     * @param routeId is identification number of route in database on data server.
     * @return array of received positions.
     */
    fun getPositions(routeId: Long): Array<Position>
}