package com.actualization.document.tasks.delegate

import com.actualization.document.tasks.exception.MissingParameterException
import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

abstract class AbstractDelegate : JavaDelegate {

    fun DelegateExecution.build(vararg paramNames: String = emptyArray(), action: ExecutionBuilder.() -> Unit) {
        with(ExecutionBuilder(this, paramNames.toSet())) {
            runCatching {
                logger.info { "Started process with params '$params'" }
                action(this)
                logger.info { "Finished process with params '$params'" }
            }.onFailure {
                logger.warn(it) { "Execution failed and will be retried with params '$params'" }
            }.getOrThrow()
        }
    }

    inner class ExecutionBuilder(
        val execution: DelegateExecution,
        paramNames: Set<String>,
        val processId: String = execution.processInstanceId!!,
        val clientId: String? = execution.getVariable(CLIENT_ID)?.let { it as String }
    ) {
        val params: Map<String, Any> = execution.variables.filter { it.key in paramNames.plus(CLIENT_ID) }

        inline fun <reified T> getParam(paramName: String): T {
            val result = params[paramName] as T?
            logger.info { "Getting param '$paramName' result is '$result'" }
            return result ?: run {
                logger.warn { "Not found parameter '$paramName', params: '$params'" }
                throw MissingParameterException(paramName)
            }
        }

        inline fun <reified T> getNullableParam(paramName: String): T? {
            return execution.getVariable(paramName)?.let { it as T }.also {
                logger.info { "Getting nullable param '$paramName' result is '$it'" }
            }
        }

        fun setParams(vararg newParams: Pair<String, Any?>) {
            newParams.forEach { (key, value) ->
                execution.setVariable(key, value)
                logger.info { "Set param '$key' to value '$value'" }
            }
        }
    }

    companion object : KLogging() {
        const val CLIENT_ID = "clientId"
    }
}
