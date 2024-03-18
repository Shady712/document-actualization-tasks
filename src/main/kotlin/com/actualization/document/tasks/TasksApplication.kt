package com.actualization.document.tasks

import com.actualization.document.tasks.config.CamundaDeploymentProperties
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableProcessApplication
@EnableConfigurationProperties(
	CamundaDeploymentProperties::class
)
class TasksApplication

fun main(args: Array<String>) {
	runApplication<TasksApplication>(*args)
}
