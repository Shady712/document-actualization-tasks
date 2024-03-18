package com.actualization.document.tasks.delegate

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.stereotype.Component

@Component
class ChildExtensionSearchDelegate : AbstractDelegate() {

    override fun execute(execution: DelegateExecution) {
        execution.build {
            logger.info { "I am a stub!" }
            setParams(CHILD_ID to null)
        }
    }

    companion object : KLogging() {
        const val CHILD_ID = "childId"
    }
}
