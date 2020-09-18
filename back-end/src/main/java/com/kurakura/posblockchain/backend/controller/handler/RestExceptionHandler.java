package com.kurakura.posblockchain.backend.controller.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import com.kurakura.posblockchain.backend.exception.BadRequestParameterException;
import com.kurakura.posblockchain.backend.exception.BusinessLogicFailureException;
import com.kurakura.posblockchain.infra.constants.ErrorCode;
import com.kurakura.posblockchain.infra.entity.ErrorMsg;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadRequestParameterException.class)
    public ResponseEntity<ErrorMsg> handleBadRequestParameterException(HttpServletRequest req, BadRequestParameterException e){
        ErrorMsg msg = createErrorMsg(e.getErrorCode(), e.getErrorDescription());
        return new ResponseEntity<ErrorMsg>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessLogicFailureException.class)
    public ResponseEntity<ErrorMsg> handleBusinessLogicFailureException(HttpServletRequest req, BusinessLogicFailureException e){
        ErrorMsg msg = createErrorMsg(e.getErrorCode(), e.getErrorDescription());
        return new ResponseEntity<ErrorMsg>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        e.getConstraintViolations().forEach(ex -> 
            errors.add(ex.getPropertyPath() + ": " + ex.getMessage()));
        ErrorMsg msg = createErrorMsg(ErrorCode.ER_400_2, errors.toString());
        return new ResponseEntity<Object>(msg, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorMsg msg = createErrorMsg(ErrorCode.ER_400_2, errors.toString());
        return new ResponseEntity<Object>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException e, WebRequest request) {
        log.error(String.format("Database acess error. %s: %s", e.getClass(), e.getMessage()));
        ErrorMsg msg = createErrorMsg(ErrorCode.ER_500_1, "Database acess error.");
        return new ResponseEntity<Object>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception e, WebRequest request) {
        log.error(String.format("API process resulted in failure. %s: %s", e.getClass(), e.getMessage()));
        ErrorMsg msg = createErrorMsg(ErrorCode.ER_500_2, "API process resulted in failure.");
        return new ResponseEntity<Object>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorMsg createErrorMsg(ErrorCode code, String description) {
        return new ErrorMsg(code.toString(), description);
    }

}