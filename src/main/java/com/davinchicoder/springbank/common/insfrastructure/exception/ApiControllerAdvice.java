package com.davinchicoder.springbank.common.insfrastructure.exception;

import com.davinchicoder.springbank.common.insfrastructure.exception.base.BadRequestException;
import com.davinchicoder.springbank.common.insfrastructure.exception.base.ConflictException;
import com.davinchicoder.springbank.common.insfrastructure.exception.base.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ErrorMessage badRequest(HttpServletRequest request, Exception exception) {

        return new ErrorMessage(exception.getMessage(), exception.getClass().getSimpleName(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    public ErrorMessage conflict(HttpServletRequest request, Exception exception) {

        return new ErrorMessage(exception.getMessage(), exception.getClass().getSimpleName(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorMessage notFound(HttpServletRequest request, Exception exception) {

        return new ErrorMessage(exception.getMessage(), exception.getClass().getSimpleName(), request.getRequestURI());
    }

}
