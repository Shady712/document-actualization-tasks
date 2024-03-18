package com.actualization.document.tasks.exception

class MissingParameterException(
    parameterName: String
) : RuntimeException("Parameter '$parameterName' not found in execution context")