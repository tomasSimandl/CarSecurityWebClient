package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Token

/**
 * Service which is used for sending requests to authorization server.
 */
interface LoginService {

    /**
     * Method sends login request to authorization server with input credentials.
     *
     * @param username of user which is trying to login.
     * @param password which is used for authorization.
     * @return Class token which contains all requested tokens.
     */
    fun login(username: String, password: String): Token

    /**
     * Method send request to refresh actual token to authorization server.
     *
     * @param refreshToken which will be used to obtain new access token.
     */
    fun refresh(refreshToken: String): Token

    /**
     * Method send register user request to authorization server. Http status code is returned as a result.
     *
     * @param username of new user.
     * @param password of new user.
     * @return http status code of this operation.
     */
    fun register(username: String, password: String): Int
}