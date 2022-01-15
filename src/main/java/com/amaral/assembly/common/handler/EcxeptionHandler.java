package com.amaral.assembly.common.handler;

import com.amaral.assembly.common.context.LocaleContext;
import com.amaral.assembly.common.domain.StandardError;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class EcxeptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException ex, HttpServletRequest request) {
        StandardError error = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), i18n(ex.getMessage()), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    private String i18n(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContext.get());
        } catch (Exception e) {
            return key;
        }
    }

}
