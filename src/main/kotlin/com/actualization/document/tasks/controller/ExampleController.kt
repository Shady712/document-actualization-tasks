package com.actualization.document.tasks.controller

import com.actualization.document.tasks.client.ExampleClient
import com.actualization.document.tasks.dto.ExampleResponseDto
import com.actualization.document.tasks.service.CamundaEngineService
import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/example")
class ExampleController(
    private val camundaEngineService: CamundaEngineService,
    private val exampleClient: ExampleClient
) {

    @PostMapping("/start")
    fun startExampleProcess(): ExampleResponseDto {
        return ExampleResponseDto(camundaEngineService.startProcess(EXAMPLE_PROCESS_NAME).processInstanceId)
    }

    @PostMapping("/client")
    fun client(): ExampleResponseDto? {
        return exampleClient.example()
    }

    companion object : KLogging() {
        const val EXAMPLE_PROCESS_NAME = "ExampleProcess"
    }
}
