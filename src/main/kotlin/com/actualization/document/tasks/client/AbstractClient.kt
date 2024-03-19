package com.actualization.document.tasks.client

import com.actualization.document.tasks.exception.ApiInternalException
import com.actualization.document.tasks.exception.ApiRequestException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogger
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.http.HttpStatus
import java.io.IOException

abstract class AbstractClient(
    private val objectMapper: ObjectMapper,
    private val name: String,
    private val logger: KLogger
) {

    private val client = OkHttpClient.Builder().build()

    protected fun <T> doCall(request: Request, typeClass: Class<T>): T? {
        try {
            execute(request).use { response ->
                checkResponseCode(response)

                return objectMapper.readValue(response.body!!.byteStream(), typeClass)
            }
        } catch (ex: IOException) {
            logger.error("$name service internal error", ex)
            throw ApiInternalException(cause = ex)
        }
    }

    private fun execute(request: Request): Response = client.newCall(request).execute()

    private fun checkResponseCode(response: Response) {
        val status = HttpStatus.valueOf(response.code)

        if (status.is4xxClientError) {
            val message = response.body!!.byteStream().reader().readText()
            logger.error("$name client error. HttpStatusCode ${response.code}, message $message")
            throw ApiRequestException("$name client error: $message")
        }

        if (status.is5xxServerError) {
            val message = response.body!!.byteStream().reader().readText()
            logger.error("$name service internal error with code ${response.code}. Body: $message")
            throw ApiInternalException("$name internal error")
        }
    }
}