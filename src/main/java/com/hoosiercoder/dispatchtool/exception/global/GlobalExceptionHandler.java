package com.hoosiercoder.dispatchtool.exception.global;

import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: HoosierCoder
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    //HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Invalid Parameter");

        String parameterName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        Object invalidValue = ex.getValue();

        // Check if the mismatch is due to an Enum
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String enumValues = "";//getEnumValues(ex.getRequiredType());

            if (ex.getRequiredType().equals(UserRole.class)) {

                for (String role : UserRole.getRoleNames()) {
                    if (enumValues.length() != 0) {
                        enumValues += ", ";
                    }
                    enumValues += role;
                }
                //enumValues = UserRole.values().toString();//roleNames.toString();
            }
            body.put("message", String.format("Invalid value '%s' for parameter '%s'. Valid values are: %s",
                    invalidValue, parameterName, enumValues));
        } else {
            body.put("message", String.format("Parameter '%s' has an invalid value '%s'. Required type is '%s'.",
                    parameterName, invalidValue, requiredType));
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
