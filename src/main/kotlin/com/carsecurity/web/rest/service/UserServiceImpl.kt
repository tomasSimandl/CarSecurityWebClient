package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class UserServiceImpl(

        @Value("\${auth.server.url}")
        private val authorizationServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : UserService {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun getUser(username: String): User {

        val url = "$authorizationServerUrl$USER_MAPPING?username=$username"

        val userEntity = restTemplate.getForEntity(url, User::class.java)
        return userEntity.body!!
    }

    override fun updateUserEmail(userId: Long, email: String){
        val url = "$authorizationServerUrl$USER_MAPPING?id=$userId&email=$email"

        logger.debug("Sending change email request. ID: $userId, EMAIL: $email")

        restTemplate.put(url, null)
    }

    override fun removeUser(userId: Long) {
        val url = "$authorizationServerUrl$USER_MAPPING?id=$userId"
        restTemplate.delete(url)
    }
}