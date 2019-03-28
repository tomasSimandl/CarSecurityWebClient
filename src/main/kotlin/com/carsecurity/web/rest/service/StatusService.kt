package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Status


/**
 * Service which is used for sending requests to data server.
 */
interface StatusService {

    /**
     * Method send request to get status from device to data server.
     *
     * @param carId is identification number of car to which should be send status request.
     * @return [Status] from device.
     */
    fun getStatus(carId: Long): Status
}