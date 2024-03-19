package com.actualization.document.tasks.client

import com.actualization.document.tasks.dto.ExampleResponseDto
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Component

@Component
class ExampleClient(
    objectMapper: ObjectMapper
) : AbstractClient(objectMapper, "Example", logger) {

    fun example(): ExampleResponseDto? {
        val request = Request.Builder()
            .post("".toRequestBody("application/json".toMediaType()))
            .url("http://localhost:8080/api/v1/example/start")
            .build()

        return doCall(request, ExampleResponseDto::class.java)
    }

    companion object : KLogging()
}
