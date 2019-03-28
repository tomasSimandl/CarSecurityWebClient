package com.carsecurity.web.config

import com.carsecurity.web.rest.model.Token
import com.carsecurity.web.rest.service.LoginService
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

/**
 * Class used for users authentication with authorization server.
 *
 * @param loginService is service used for sending login and register request to authorization server.
 * @param httpSession is http session to which is stored OAuth token.
 */
@Component
class CustomAuthenticationProvider(
        private val loginService: LoginService,
        private val httpSession: HttpSession
) : AuthenticationProvider {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(CustomAuthenticationProvider::class.java)

    /** Login error when credentials are incorrect. */
    private val loginCredentialsError = "Invalid credentials."
    /** General error message when user can not be login and error is undefined. */
    private val loginGeneralError = "Can not login. Try again later."


    /**
     * Method try login user in authentication server. When authentication is successful Oauth token response is saved
     * to session. On error [BadCredentialsException] is thrown.
     *
     * @param authentication which should contains username in name attribute and password in credentials attribute.
     * @return [UsernamePasswordAuthenticationToken] with users credentials.
     */
    override fun authenticate(authentication: Authentication): Authentication? {

        val token = getToken(authentication.name, authentication.credentials.toString())
        httpSession.setAttribute("token", token)

        return UsernamePasswordAuthenticationToken(authentication.name, authentication.credentials.toString(), ArrayList<GrantedAuthority>())
    }

    /**
     * Method sends login request to authorization server over [loginService]. On success is returned [Token].
     * On error is thrown [BadCredentialsException] with error.
     *
     * @param username of user which wants to login.
     * @param password of user which wants to login.
     * @return token which was returned by authorization server.
     */
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

    /**
     * Method parse error request from [responseBody] which should be json.
     *
     * @param responseBody is json with error message from authorization server.
     * @return parsed error or [loginGeneralError].
     */
    private fun getDescription(responseBody: String): String {
        try {
            val json = JsonParser().parse(responseBody).asJsonObject
            return json["error_description"]?.asString ?: loginGeneralError

        } catch (e: Exception) {
            logger.error("Can not parse error response.", e)
            throw BadCredentialsException(loginGeneralError)
        }
    }

    /**
     * Method returns if [authentication] is supported class by this Authentication provider.
     *
     * @param authentication class of any authentication.
     * @return true when [authentication] is [UsernamePasswordAuthenticationToken].
     */
    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}