package com.actualization.document.tasks.client

import com.actualization.document.tasks.dto.TaskResponseDto
import com.actualization.document.tasks.enumeration.TaskStatus
import com.actualization.document.tasks.enumeration.TaskType
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import okhttp3.Request
import org.springframework.stereotype.Component

@Component
class TaskClient(
    objectMapper: ObjectMapper,
) : AbstractClient(objectMapper, "Task", logger) {

    fun getTask(clientId: String, taskType: TaskType, taskStatus: TaskStatus): TaskResponseDto? {
        val request = Request.Builder()
            .get()
            .url("http://localhost:8443/tasks/find?clientId=$clientId&taskType=$taskType&status=$taskStatus")
            .header("Accept-Encoding", "identity")
            .build()

        return doCall(request, TaskResponseDto::class.java)
    }

    companion object : KLogging()
}
