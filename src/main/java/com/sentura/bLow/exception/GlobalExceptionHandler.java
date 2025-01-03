package com.sentura.bLow.exception;

import com.sentura.bLow.constants.AppConstants;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.exception.types.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ResponseDto("400",ex.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ResponseDto> handleBadCredentialException(BadCredentialsException ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ResponseDto("500","Incorrect email or password!"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public final ResponseEntity<ResponseDto> userDisabledException(DisabledException ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ResponseDto("500","User disabled!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public final ResponseEntity<ResponseDto> emailNotFoundException(EmailNotFoundException ex) {
        ex.printStackTrace();
        log.error("The Given Email Address Not Found!");
        return new ResponseEntity<>(new ResponseDto("500","The Given Email Address Not Found!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailNotValidException.class)
    public final ResponseEntity<ResponseDto> emailNotValidException(EmailNotValidException ex) {
        ex.printStackTrace();
        log.error("Please Insert a Valid Email Address!");
        return new ResponseEntity<>(new ResponseDto("500","Please Insert a Valid Email Address!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ResponseDto> userNotFoundException(UserNotFoundException ex) {
        ex.printStackTrace();
        log.error("Given user id not found!");
        return new ResponseEntity<>(new ResponseDto("500","Given User ID Not Found!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public final ResponseEntity<ResponseDto> userNotVerifiedException(UserNotVerifiedException ex) {
        ex.printStackTrace();
        log.error("Given user not verified!");
        return new ResponseEntity<>(new ResponseDto("500","Given User Not Verified!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotActiveException.class)
    public final ResponseEntity<ResponseDto> userNotActiveException(UserNotActiveException ex) {
        ex.printStackTrace();
        log.error("Given user not active!");
        return new ResponseEntity<>(new ResponseDto("500","Given User Not Active!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ResponseDto> handleAllExceptions(Throwable ex) throws UserNotVerifiedException{
        if (ex.getCause() instanceof UserNotVerifiedException) {
            log.error("Given user not verified!");
            return new ResponseEntity<>(new ResponseDto("USER_NOT_VERIFIED","500", "Given User Not Verified!"), HttpStatus.BAD_REQUEST);
        } else if (ex.getCause() instanceof UserNotActiveException) {
            log.error("Given user not active!");
            return new ResponseEntity<>(new ResponseDto("USER_NOT_ACTIVATED","500","User not active! Please contact the administrator."), HttpStatus.BAD_REQUEST);
        } else {
            ex.printStackTrace();
            log.error(ex.getMessage());
            return new ResponseEntity<>(new ResponseDto("500", AppConstants.SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}