package com.actualization.document.tasks.bpmn

import com.actualization.document.tasks.controller.TaskCreationController.Companion.TASK_CREATION_PROCESS_NAME
import org.camunda.bpm.engine.test.Deployment
import org.camunda.community.mockito.DelegateExpressions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val BPMN_FILE = "bpmn/taskCreation.bpmn"

@Deployment(resources = [BPMN_FILE])
class TaskCreationBpmnTest : AbstractBpmnTest() {

    @BeforeEach
    fun beforeEach() {
        DelegateExpressions.autoMock(BPMN_FILE)
    }

    @Test
    fun `Create task and child extension`() {
        with(startProcess(TASK_CREATION_PROCESS_NAME)) {
            assertSequentialExecution(
                TASK_CREATION_START_EVENT,
                FORCE_CREATE_GATEWAY,
                TASK_SEARCH_ACTIVITY,
                TASK_FOUND_GATEWAY,
                CREATE_TASK_GATEWAY,
                CREATE_TASK_ACTIVITY,
                CHILD_EXTENSION_FOUND_GATEWAY,
                CREATE_CHILD_EXTENSION_ACTIVITY,
                CREATED_CHILD_EXTENSION_GATEWAY,
                TASK_COMPLETE_GATEWAY,
                COMPLETED_TASK_GATEWAY,
                TASK_CREATION_END_EVENT
            )

            assertProcessEndState(ended = true)
        }
    }

    companion object {
        private const val TASK_CREATION_START_EVENT = "TaskCreationStartEvent"
        private const val TASK_CREATION_END_EVENT = "TaskCreationEndEvent"

        private const val FORCE_CREATE_GATEWAY = "ForceCreateGateway"
        private const val TASK_FOUND_GATEWAY = "TaskFoundGateway"
        private const val CREATE_TASK_GATEWAY = "CreateTaskGateway"
        private const val CHILD_EXTENSION_FOUND_GATEWAY = "ChildExtensionFoundGateway"
        private const val CREATED_CHILD_EXTENSION_GATEWAY = "CreatedChildExtensionGateway"
        private const val TASK_COMPLETE_GATEWAY = "TaskCompleteGateway"
        private const val COMPLETED_TASK_GATEWAY = "CompletedTaskGateway"

        private const val TASK_SEARCH_ACTIVITY = "TaskSearchActivity"
        private const val CREATE_TASK_ACTIVITY = "CreateTaskActivity"
        private const val CREATE_CHILD_EXTENSION_ACTIVITY = "CreateChildExtensionActivity"
    }
}
