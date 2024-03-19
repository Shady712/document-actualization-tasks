package com.actualization.document.tasks.controller

import com.actualization.document.tasks.client.TaskClient
import com.actualization.document.tasks.dto.TaskResponseDto
import com.actualization.document.tasks.enumeration.TaskStatus
import com.actualization.document.tasks.enumeration.TaskType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/task-client")
class TaskClientController(
    private val taskClient: TaskClient
) {

    @GetMapping("/get")
    fun get(): TaskResponseDto? {
        return taskClient.getTask("1234", TaskType.A, TaskStatus.READY)
    }
}