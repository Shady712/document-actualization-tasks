package com.actualization.document.tasks.bpmn

import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration
import org.camunda.bpm.engine.impl.history.HistoryLevel
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension
import org.camunda.bpm.engine.test.mock.MockExpressionManager
import org.camunda.community.mockito.ProcessExpressions
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin
import org.junit.jupiter.api.extension.RegisterExtension

val processEngine: ProcessEngine = StandaloneInMemProcessEngineConfiguration()
    .setJobExecutorActivate(false)
    .setExpressionManager(MockExpressionManager())
    .setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_DROP_CREATE)
    .setJdbcDriver("org.postgresql.Driver")
    .setJdbcUsername("tasks-db")
    .setJdbcPassword("admin")
    .setJdbcUrl("jdbc:postgresql://localhost:5432/tasks")
    .setDbMetricsReporterActivate(false)
    .setIdGenerator(StrongUuidGenerator())
    .apply {
        processEnginePlugins.add(SpinProcessEnginePlugin())
        historyLevel = HistoryLevel.HISTORY_LEVEL_FULL
    }
    .buildProcessEngine()

abstract class AbstractBpmnTest {

    @RegisterExtension
    val extension = ProcessEngineExtension
        .builder()
        .useProcessEngine(processEngine)
        .build()

    protected fun startProcess(
        processDefinitionKey: String,
        variables: Map<String, Any?> = mapOf()
    ): ProcessInstance =
        BpmnAwareTests.runtimeService()
            .startProcessInstanceByKey(processDefinitionKey, variables)

    protected fun startProcessBeforeActivity(
        processDefinitionKey: String,
        activityId: String,
        variables: Map<String, Any?> = mapOf()
    ): ProcessInstance =
        ProcessEngineTests
            .runtimeService()
            .createProcessInstanceByKey(processDefinitionKey)
            .setVariables(variables)
            .startBeforeActivity(activityId)
            .execute()

    protected fun registerCallActivityMock(processDefinitionKey: String, activityId: String) {
        extension.manageDeployment(
            ProcessExpressions.registerCallActivityMock(processDefinitionKey)
                .onExecutionWaitForMessage(activityId)
                .deploy(processEngine)
        )
    }

    protected fun registerCallActivityMockWithOnExecution(
        processDefinitionKey: String,
        action: (DelegateExecution) -> Unit
    ) {
        extension.manageDeployment(
            ProcessExpressions.registerCallActivityMock(processDefinitionKey)
                .onExecutionDo(action)
                .deploy(processEngine)
        )
    }

    protected fun registerCallActivityMockWithOnExecutionAndWaitForMessage(
        processDefinitionKey: String,
        message: String,
        action: (DelegateExecution) -> Unit
    ) {
        extension.manageDeployment(
            ProcessExpressions.registerCallActivityMock(processDefinitionKey)
                .onExecutionDo(action)
                .onExecutionWaitForMessage(message)
                .deploy(processEngine)
        )
    }
}