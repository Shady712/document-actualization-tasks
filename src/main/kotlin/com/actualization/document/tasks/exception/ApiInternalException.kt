package com.actualization.document.tasks.exception

class ApiInternalException(
    message: String? = null,
    cause: Exception? = null
) : RuntimeException(message, cause)