package com.carsecurity.web.config

import com.carsecurity.web.rest.service.LoginService
import com.carsecurity.web.rest.model.Token
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import java.util.*
import javax.servlet.http.HttpSession

@Component
class CustomAuthenticationProvider(
        private val loginService: LoginService,
        private val httpSession: HttpSession
) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(CustomAuthenticationProvider::class.java)

    private val loginCredentialsError = "Invalid credentials."
    private val loginGeneralError = "Can not login. Try again later."


    override fun authenticate(authentication: Authentication): Authentication? {

        val token = getToken(authentication.name, authentication.credentials.toString())
        httpSession.setAttribute("token", token)

        return UsernamePasswordAuthenticationToken(authentication.name, authentication.credentials.toString(), ArrayList<GrantedAuthority>())
    }

    private fun getToken(username: String, password: String): Token {
        try {
            return loginService.login(username, password)

        } catch (e: HttpStatusCodeException) {
            logger.debug("Can not login. Server response status code: ${e.statusCode} $")

            if (e.statusCode == HttpStatus.UNAUTHORIZED) {
                throw BadCredentialsException(loginCredentialsError)
            } else {
                throw BadCredentialsException(getDescription(e.responseBodyAsString))
            }
        } catch (e: Exception) {
            logger.error("Can not login cause exception", e)
            throw BadCredentialsException(loginGeneralError)
        }
    }

    private fun getDescription(responseBody: String): String {
        try {
            val json = JsonParser().parse(responseBody).asJsonObject
            return json["error_description"]?.asString ?: loginGeneralError

        } catch (e: Exception) {
            logger.error("Can not parse error response.", e)
            throw BadCredentialsException(loginGeneralError)
        }
    }


    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}