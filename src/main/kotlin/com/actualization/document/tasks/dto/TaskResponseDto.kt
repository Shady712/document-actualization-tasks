package com.actualization.document.tasks.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TaskResponseDto(
    @JsonProperty("taskId")
    val taskId: String?,
    @JsonProperty("taskType")
    val taskType: String?,
    @JsonProperty("clientId")
    val clientId: String?,
    @JsonProperty("status")
    val taskStatus: String?,
    @JsonProperty("childExtensions")
    val childExtensions: List<ChildExtensionResponseDto> = emptyList()
)
