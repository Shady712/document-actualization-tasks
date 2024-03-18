package com.actualization.document.tasks.service

import org.apache.commons.lang3.RandomStringUtils
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.stereotype.Service

@Service
class CamundaEngineService(
    private val runtimeService: RuntimeService
) {

    fun startProcess(processName: String, vars: Map<String, Any> = mutableMapOf()): ProcessInstance =
        runtimeService.startProcessInstanceByKey(
            processName,
            RandomStringUtils.randomAlphanumeric(10),
            vars
        )
}
