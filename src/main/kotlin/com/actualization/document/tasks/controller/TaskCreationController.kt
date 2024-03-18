package com.actualization.document.tasks.controller

import com.actualization.document.tasks.dto.ExampleResponseDto
import com.actualization.document.tasks.service.CamundaEngineService
import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/task-creation")
class TaskCreationController(
    private val camundaEngineService: CamundaEngineService
) {

    @PostMapping("/start")
    fun startTaskCreationProcess(): ExampleResponseDto {
        return ExampleResponseDto(camundaEngineService.startProcess(TASK_CREATION_PROCESS_NAME).processInstanceId)
    }

    companion object : KLogging() {
        const val TASK_CREATION_PROCESS_NAME = "TaskCreationProcess"
    }
}