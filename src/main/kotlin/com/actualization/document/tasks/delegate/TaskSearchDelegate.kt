package com.actualization.document.tasks.delegate

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.stereotype.Component

@Component
class TaskSearchDelegate : AbstractDelegate() {

    override fun execute(execution: DelegateExecution) {
        execution.build {
            logger.info { "I am a stub!" }
            setParams(TASK_ID to null)
        }
    }

    companion object : KLogging() {
        const val TASK_ID = "taskId"
    }
}
