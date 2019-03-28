package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.User

/**
 * Implementation of service which is used for sending requests to authorization server.
 */
interface UserService {

    /**
     * Method send request to get users information to authorization server.
     *
     * @parma username of requested user.
     * @return found user from database on authorization server.
     */
    fun getUser(username: String): User

    /**
     * Method send request to authorization server to update users email.
     *
     * @param userId is identification of user in database on authorization server which email will be changed.
     * @param email new email which will be set to user.
     */
    fun updateUserEmail(userId: Long, email: String)

    /**
     * Method send request to data server to delete user from database on authorization server.
     *
     * @param userId is identification number of user on authorization server which will be deleted.
     */
    fun removeUser(userId: Long)
}