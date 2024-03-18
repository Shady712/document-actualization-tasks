package com.actualization.document.tasks.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("camunda.bpm.deployment")
data class CamundaDeploymentProperties(
    var path: String,
    var changesOnly: Boolean
)
