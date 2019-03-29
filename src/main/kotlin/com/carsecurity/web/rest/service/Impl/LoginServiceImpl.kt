package com.carsecurity.web.rest.service.Impl

import com.carsecurity.web.rest.model.Token
import com.carsecurity.web.rest.service.LoginService
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * Implementation of service which is used for sending requests to authorization server.
 *
 * @param authorizationServerUrl is url address of authorization server
 * @param clientId is identification of this application as OAuth client.
 * @param clientSecret is secret for this application as OAuth client.
 * @param notLoginRestTemplate rest template for sending requests without authorization.
 */
@Service
class LoginServiceImpl(

        @Value("\${auth.server.url}")
        private val authorizationServerUrl: String,

        @Value("\${auth.client.id}")
        private val clientId: String,

        @Value("\${auth.client.secret}")
        private val clientSecret: String,

        @Qualifier("loginRestTemplate")
        private val notLoginRestTemplate: RestTemplate
) : LoginService {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(LoginServiceImpl::class.java)

    /**
     * Method sends login request to authorization server with input credentials.
     *
     * @param username of user which is trying to login.
     * @param password which is used for authorization.
     * @return Class token which contains all requested tokens.
     */
    override fun login(username: String, password: String): Token {

        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_FORM_URLENCODED
        header.setBasicAuth(clientId, clientSecret)

        val form = LinkedMultiValueMap<String, String>()
        form["username"] = username
        form["password"] = password
        form["grant_type"] = "password"

        val httpEntity = HttpEntity(form, header)

        logger.debug("Sending login request to authorization server.")

        val tokenEntity = notLoginRestTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
        return tokenEntity.body!!
    }

    /**
     * Method send request to refresh actual token to authorization server.
     *
     * @param refreshToken which will be used to obtain new access token.
     */
    override fun refresh(refreshToken: String): Token {
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_FORM_URLENCODED
        header.setBasicAuth(clientId, clientSecret)

        val form = LinkedMultiValueMap<String, String>()
        form["refresh_token"] = refreshToken
        form["grant_type"] = "refresh_token"

        val httpEntity = HttpEntity(form, header)
        logger.debug("Sending refresh token request to authorization server.")

        val tokenEntity = notLoginRestTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
        return tokenEntity.body!!
    }

    /**
     * Method send register user request to authorization server. Http status code is returned as a result.
     *
     * @param username of new user.
     * @param password of new user.
     * @return http status code of this operation.
     */
    override fun register(username: String, password: String): Int {

        val token = getClientCredentialsToken()
        if (!token.isPresent) {
            return 500
        }

        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_JSON
        header["Authorization"] = "${token.get().tokenType} ${token.get().accessToken}"

        val json = JsonObject()
        json.addProperty("username", username)
        json.addProperty("password", password)
        val jsonRoles = JsonArray(1)
        jsonRoles.add("ROLE_USER")
        json.add("roles", jsonRoles)


        val httpEntity = HttpEntity(json.toString(), header)
        logger.debug("Sending registration request to authorization server.")

        try {
            val entity = notLoginRestTemplate.postForEntity(authorizationServerUrl + USER_MAPPING, httpEntity, String::class.java)
            return entity.statusCodeValue

        } catch (e: HttpStatusCodeException) {
            logger.debug("Server response with status code: ${e.statusCode}")
            return e.statusCode.value()
        }
    }

    /**
     * This method get access token from authorization token for this application as client which can register new
     * users.
     *
     * @return [Optional] with received token or empty optional on error.
     */
    private fun getClientCredentialsToken(): Optional<Token> {
        try {
            val header = HttpHeaders()
            header.contentType = MediaType.APPLICATION_FORM_URLENCODED
            header.setBasicAuth(clientId, clientSecret)

            val form = LinkedMultiValueMap<String, String>()
            form["client_id"] = clientId
            form["client_secret"] = clientSecret
            form["grant_type"] = "client_credentials"

            val httpEntity = HttpEntity(form, header)

            logger.debug("Sending client credentials request to authorization server.")

            val tokenEntity = notLoginRestTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
            return Optional.of(tokenEntity.body!!)

        } catch (e: Exception) {
            logger.error("Can not login in authorization server.", e)
            return Optional.empty()
        }
    }
}