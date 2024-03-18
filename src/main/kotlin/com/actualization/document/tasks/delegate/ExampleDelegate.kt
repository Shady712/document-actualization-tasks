package com.actualization.document.tasks.delegate

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class ExampleDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        logger.info { "I am an example delegate, running process '${execution.processInstance.processInstanceId}'" }
    }

    companion object : KLogging()
}
