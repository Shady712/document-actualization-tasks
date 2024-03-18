package com.actualization.document.tasks.bpmn

import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class ActivityAndAction(
    val name: String,
    val executeJob: Boolean = true,
    val action: () -> Unit = {},
)

data class ActivityAndMessage(
    val name: String,
    val message: String,
    val eventBasedGateway: Boolean
)

data class ActivityAndPromoteTime(
    val name: String,
    val time: LocalDateTime
)

@Suppress("ArrayInDataClass")
data class IsNotWaitingActivities(
    val names: Array<String>
)

@Suppress("ArrayInDataClass")
data class IsWaitingExactlyActivities(
    val names: Array<String>
)

data class ProcessEndState(
    val ended: Boolean
)

fun ProcessInstance.assertSequentialExecution(vararg activities: Any) {
    activities.forEach { activity ->
        when (activity) {
            is String -> assertIsWaitingAt(arrayOf(activity))
            is ActivityAndAction -> assertIsWaitingAt(
                arrayOf(activity.name),
                false,
                activity.executeJob,
                activity.action
            )
            is ActivityAndMessage -> assertIsWaitingAtAndCorrelateMessage(
                activity.name,
                activity.message,
                activity.eventBasedGateway
            )
            is ActivityAndPromoteTime -> assertIsWaitingAtAndPromoteTime(activity.name, activity.time)
            is IsNotWaitingActivities -> assertIsNotWaitingAt(activity.names)
            is IsWaitingExactlyActivities -> assertIsWaitingAt(activity.names, true)
            is ProcessEndState -> assertProcessEndState(activity.ended)
            else -> throw IllegalArgumentException(
                "Only String, CorrelateMessage, TaskAndMessage and TaskAndAction are acceptable"
            )
        }
    }
}

fun ProcessInstance.assertProcessEndState(
    ended: Boolean
) {
    val processInstanceAssert = BpmnAwareTests.assertThat(this)

    if (ended) {
        processInstanceAssert.isEnded
    } else {
        processInstanceAssert.isNotEnded
    }
}

fun ProcessInstance.assertIsWaitingAt(
    activityIds: Array<String>,
    exactly: Boolean = false,
    executeJob: Boolean = true,
    afterEvaluationAction: () -> Unit = {},
) {
    if (exactly) {
        BpmnAwareTests.assertThat(this).isWaitingAtExactly(*activityIds)
    } else {
        BpmnAwareTests.assertThat(this).isWaitingAt(*activityIds)
    }

    afterEvaluationAction.invoke()

    if (executeJob) {
        activityIds.forEach {
            try {
                val job = BpmnAwareTests.job(it)
                BpmnAwareTests.execute(job)
            } catch (ex: IllegalArgumentException) {
                throw RuntimeException("Programmer error on awaiting for task <$it>: ${ex.message}", ex)
            } catch (ex: ProcessEngineException) {
                throw RuntimeException("BPMN schema error on awaiting for task <$it>: ${ex.message}", ex)
            }
        }
    }
}

fun ProcessInstance.assertIsNotWaitingAt(
    activityIds: Array<String>,
) {
    BpmnAwareTests.assertThat(this).isNotWaitingAt(*activityIds)
}

fun ProcessInstance.assertIsWaitingAtAndCorrelateMessage(
    taskName: String,
    messageName: String,
    executeJob: Boolean = false,
    afterEvaluationAction: () -> Unit = {},
) {
    assertIsWaitingAt(arrayOf(taskName), false, executeJob, afterEvaluationAction)
    createAndCorrelateMessage(messageName)
}

fun ProcessInstance.assertIsWaitingAtAndPromoteTime(
    taskName: String,
    time: LocalDateTime
) {
    BpmnAwareTests.assertThat(this).isWaitingAt(taskName)

    promoteTime(time)
}

fun promoteTime(time: LocalDateTime) {
    val dueDate = Date.from(time.atZone(ZoneId.systemDefault()).toInstant())

    val jobs = ProcessEngineTests.jobQuery().active().timers().list()

    jobs.filter {
        it.duedate <= dueDate
    }.forEach {
        BpmnAwareTests.execute(it)
    }
}

fun createAndCorrelateMessage(messageName: String) =
    BpmnAwareTests.runtimeService().createMessageCorrelation(messageName).correlate()

infix fun String.withoutExecution(that: () -> Unit) = ActivityAndAction(this, false, that)
