package com.hostel.crm.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: EntityNotFoundException) =
        ErrorResponse(exception.message)

    @ExceptionHandler(EntityAlreadyExistException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleAlreadyExist(exception: EntityAlreadyExistException) =
        ErrorResponse(exception.message)

}