package com.actualization.document.tasks.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChildExtensionResponseDto(
    @JsonProperty("childId")
    val childId: String?,
    @JsonProperty("childDesc")
    val childDesc: String?,
    @JsonProperty("taskId")
    val taskId: String?
)
