package com.actualization.document.tasks.config

import mu.KLogging
import org.apache.commons.io.FilenameUtils
import org.camunda.bpm.application.ProcessApplicationInterface
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.repository.DeploymentBuilder
import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationStartedEvent
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component
import kotlin.time.measureTime

@Component
class CamundaDeployConfig(
    private val camundaDeploymentProperties: CamundaDeploymentProperties,
    private val processApplication: ProcessApplicationInterface,
    private val repositoryService: RepositoryService
) {

    @kotlin.time.ExperimentalTime
    @Suppress("UnusedPrivateMember")
    @EventListener
    fun accept(event: ProcessApplicationStartedEvent) {
        logger.info { "Starting Camunda custom deployment" }
        deployProcessArchive(camundaDeploymentProperties)
        logger.info { "Camunda custom deployment finished" }
    }

    @kotlin.time.ExperimentalTime
    private fun deployProcessArchive(processArchive: CamundaDeploymentProperties) {
        logger.info { "Deploying process archive: '$processArchive'" }

        val deploymentBuilder =
            repositoryService.createDeployment(processApplication.reference)
                .name(DEPLOYMENT_NAME)
                .source(PROCESS_APPLICATION)
                .enableDuplicateFiltering(processArchive.changesOnly)

        PathMatchingResourcePatternResolver()
            .getResources("classpath*:${processArchive.path}/**/*.*")
            .filter { isCamundaResource(it) }
            .forEach { addDeployment(processArchive, deploymentBuilder, it) }

        val deployTime = measureTime {
            deploymentBuilder.deploy()
        }

        logger.info {
            "Deployment of '${deploymentBuilder.resourceNames.size}' resources took '${deployTime.inWholeMilliseconds}' milliseconds"
        }
    }

    private fun isCamundaResource(resource: Resource) =
        CAMUNDA_FILE_SUFFIXES.any { it.equals(FilenameUtils.getExtension(resource.filename)) }

    private fun addDeployment(
        processArchive: CamundaDeploymentProperties,
        deploymentBuilder: DeploymentBuilder,
        resource: Resource
    ) = sanitizePath(resource.uri.toString(), processArchive.path)
        .also { logger.info { "Adding resource: '$it'" } }
        .let { deploymentBuilder.addClasspathResource(it) }

    private fun sanitizePath(path: String, fragment: String) = path.substring(path.indexOf(fragment))

    companion object : KLogging() {
        private const val PROCESS_APPLICATION = "process application"
        private const val DEPLOYMENT_NAME = "TasksDeployment"
        val CAMUNDA_FILE_SUFFIXES = setOf(
            CamundaBpmProperties.DEFAULT_BPMN_RESOURCE_SUFFIXES.toSet(),
            CamundaBpmProperties.DEFAULT_CMMN_RESOURCE_SUFFIXES.toSet(),
            CamundaBpmProperties.DEFAULT_DMN_RESOURCE_SUFFIXES.toSet()
        ).flatten()
    }
}
