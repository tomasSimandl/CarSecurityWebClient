package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Position


interface PositionService {

    fun getPositions(routeId: Long): Array<Position>
}