package com.carsecurity.web.config

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomErrorAttributes : DefaultErrorAttributes() {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(CustomErrorAttributes::class.java)

    /** Error message key. */
    private val errorMessage = DefaultErrorAttributes::class.java.name + ".MESSAGE"
    /** Error http status code key. */
    private val errorStatusCode = DefaultErrorAttributes::class.java.name + ".STATUS"

    /**
     * This method is used for catch all exceptions which can be thrown by controllers.
     * [HttpClientErrorException] is catch explicitly. This exception is from RestTemplate when request failed.
     * Error response is parsed or its used default error message.
     * Error message and http status codes are stored in [request] for later process in [getErrorAttributes] method.
     *
     * @param request which cause exception.
     * @param response which will be send as response to [request].
     * @param ex exception which was thrown.
     * @return null
     */
    override fun resolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: Exception): ModelAndView? {

        var message = "Can not communicate with server"
        val statusCode: Int

        if (ex is HttpClientErrorException) {
            logger.debug("Error: rest server response status code: ${ex.statusCode} $")

            if (ex.statusCode == HttpStatus.UNAUTHORIZED || ex.statusCode == HttpStatus.FORBIDDEN) {

                statusCode = HttpServletResponse.SC_UNAUTHORIZED
                message = "Not authorized for this operation."

            } else if (ex.statusCode == HttpStatus.REQUEST_TIMEOUT) {
                statusCode = HttpServletResponse.SC_REQUEST_TIMEOUT
                message = "Device is unreachable. Request timeout."
            } else {
                statusCode = ex.statusCode.value()
                try {
                    val json = JsonParser().parse(ex.responseBodyAsString) as JsonObject
                    if (json.has("error")) {
                        message = json["error"].toString().trim('"')
                    }
                } catch (e: Exception) {
                    logger.debug("Response can not be parsed to JSON.")
                }
            }
        } else {
            logger.error("Controller ends with exception:", ex)
            statusCode = HttpServletResponse.SC_BAD_REQUEST
            message = "Can not communicate with server: ${ex.message}"
        }

        request.setAttribute(errorStatusCode, statusCode)
        request.setAttribute(errorMessage, message)

        return null
    }

    /**
     * Description copied from interface: ErrorAttributes
     * Returns a Map of the error attributes. The map can be used as the model of an error page ModelAndView, or
     * returned as a ResponseBody.
     *
     * Method load status code and error message from [webRequest] which was stored in with [resolveException] method.
     * Status code is stored in webRequest and error message is returned as map with error key.
     *
     * @param webRequest the source request
     * @param includeStackTrace if stack trace elements should be included
     * @return map with key error which contains error message.
     */
    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): MutableMap<String, Any> {

        // Get data from resolveException method
        var message = webRequest.getAttribute(errorMessage, 0) as String?
        var statusCode = webRequest.getAttribute(errorStatusCode, 0) as Int?

        if (statusCode == null || statusCode == 0) {
            // Get data given by Spring
            statusCode = webRequest.getAttribute("javax.servlet.error.status_code", 0) as Int?
        }

        if (message == null || message.isBlank()) {
            message = if (statusCode != null) {
                HttpStatus.resolve(statusCode).toString()
            } else {
                "Unknown error!"
            }
        }

        if (statusCode == null || statusCode == 0) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }

        webRequest.setAttribute("javax.servlet.error.status_code", statusCode, 0)

        val map = mutableMapOf<String, Any>()
        map["error"] = message

        return map
    }
}