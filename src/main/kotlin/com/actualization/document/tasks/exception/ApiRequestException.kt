package com.actualization.document.tasks.exception

class ApiRequestException(
    message: String? = null,
    cause: Exception? = null
) : RuntimeException(message, cause)