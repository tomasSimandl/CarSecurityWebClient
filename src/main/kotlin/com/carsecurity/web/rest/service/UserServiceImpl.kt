package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Implementation of service which is used for sending requests to authorization server.
 *
 * @param authorizationServerUrl is url address of authorization server
 * @param restTemplate is template which can be used for sending authorized requests to data server.
 */
@Service
class UserServiceImpl(

        @Value("\${auth.server.url}")
        private val authorizationServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : UserService {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    /**
     * Method send request to get users information to authorization server.
     *
     * @parma username of requested user.
     * @return found user from database on authorization server.
     */
    override fun getUser(username: String): User {

        val url = "$authorizationServerUrl$USER_MAPPING?username=$username"

        val userEntity = restTemplate.getForEntity(url, User::class.java)
        return userEntity.body!!
    }

    /**
     * Method send request to authorization server to update users email.
     *
     * @param userId is identification of user in database on authorization server which email will be changed.
     * @param email new email which will be set to user.
     */
    override fun updateUserEmail(userId: Long, email: String) {
        val url = "$authorizationServerUrl$USER_MAPPING?id=$userId&email=$email"

        logger.debug("Sending change email request. ID: $userId, EMAIL: $email")

        restTemplate.put(url, null)
    }

    /**
     * Method send request to data server to delete user from database on authorization server.
     *
     * @param userId is identification number of user on authorization server which will be deleted.
     */
    override fun removeUser(userId: Long) {
        val url = "$authorizationServerUrl$USER_MAPPING?id=$userId"
        restTemplate.delete(url)
    }
}