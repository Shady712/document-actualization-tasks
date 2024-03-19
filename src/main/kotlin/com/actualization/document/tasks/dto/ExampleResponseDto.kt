package com.actualization.document.tasks.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ExampleResponseDto(
    @JsonProperty("processInstanceId")
    val processInstanceId: String
)
