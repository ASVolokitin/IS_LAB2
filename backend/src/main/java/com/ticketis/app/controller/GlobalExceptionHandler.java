package com.ticketis.app.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketis.app.dto.response.ErrorResponse;
import com.ticketis.app.exception.FailedToProcessImportException;
import com.ticketis.app.exception.FailedToUploadFileException;
import com.ticketis.app.exception.FileImportValidationException;
import com.ticketis.app.exception.NoImportProcessorException;
import com.ticketis.app.exception.PersonAlreadyOwnsThisTicketException;
import com.ticketis.app.exception.UnableToGetNecessaryFieldException;
import com.ticketis.app.exception.notfoundexception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>("HTTP method not supported", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            exception.getMessage(), "Resource not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonAlreadyOwnsThisTicketException.class)
    public ResponseEntity<?> handlePersonAlreadyOwnsThisTicketException(
            PersonAlreadyOwnsThisTicketException exception) {
        ErrorResponse errorResponse = 
        new ErrorResponse(exception.getMessage(), "Chosen person already owns this ticket");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        ErrorResponse errorResponse =
        new ErrorResponse(exception.getMessage(),
                "Operation was denied by SQL constraint violation");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException exception) {
        log.error("Transaction system error: {}", exception.getMessage());
        
        String errorMessage = "Database transaction failed";
        String details = "Could not commit transaction";
        
        Throwable rootCause = exception.getRootCause();
        if (rootCause == null) {
            rootCause = exception.getCause();
        }
        
        PSQLException psqlException = findPSQLException(rootCause);
        if (psqlException != null) {
            String psqlMessage = psqlException.getMessage();
            errorMessage = extractConstraintViolationMessage(psqlMessage);
            details = "Database constraint violation: " + errorMessage;
        } else if (rootCause instanceof DatabaseException) {
            DatabaseException dbException = (DatabaseException) rootCause;
            errorMessage = dbException.getMessage();
            details = "Database error during transaction";
            
            if (dbException.getInternalException() instanceof PSQLException) {
                PSQLException psqlEx = (PSQLException) dbException.getInternalException();
                errorMessage = extractConstraintViolationMessage(psqlEx.getMessage());
                details = "Database constraint violation: " + errorMessage;
            }
        } else if (rootCause != null) {
            errorMessage = rootCause.getMessage();
            details = "Transaction error: " + rootCause.getClass().getSimpleName();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, details);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    private PSQLException findPSQLException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        if (throwable instanceof PSQLException) {
            return (PSQLException) throwable;
        }
        return findPSQLException(throwable.getCause());
    }
    
    private String extractConstraintViolationMessage(String psqlMessage) {
        if (psqlMessage == null) {
            return "Database constraint violation";
        }

        if (psqlMessage.contains("duplicate key value violates unique constraint")) {
            String constraintName = extractConstraintName(psqlMessage);
            String keyDetails = extractKeyDetails(psqlMessage);
            
            if (keyDetails != null && constraintName != null) {
                return String.format("Duplicate value violates unique constraint: %s. %s", 
                    constraintName, keyDetails);
            } else if (constraintName != null) {
                return String.format("Duplicate value violates unique constraint: %s", constraintName);
            }
        }
        
        if (psqlMessage.contains("violates foreign key constraint")) {
            return "Foreign key constraint violation: Referenced entity does not exist";
        }
        
        if (psqlMessage.contains("violates check constraint")) {
            return "Check constraint violation: Value does not meet required conditions";
        }
        
        if (psqlMessage.contains("violates not-null constraint")) {
            return "Not-null constraint violation: Required field is missing";
        }
        
        String errorPrefix = "ERROR:";
        if (psqlMessage.contains(errorPrefix)) {
            int errorIndex = psqlMessage.indexOf(errorPrefix);
            String errorPart = psqlMessage.substring(errorIndex + errorPrefix.length()).trim();
            int newlineIndex = errorPart.indexOf('\n');
            if (newlineIndex > 0) {
                errorPart = errorPart.substring(0, newlineIndex).trim();
            }
            return errorPart;
        }
        
        return psqlMessage;
    }
    
    private String extractConstraintName(String message) {
        int start = message.indexOf('"');
        if (start >= 0) {
            int end = message.indexOf('"', start + 1);
            if (end > start) {
                return message.substring(start + 1, end);
            }
        }
        return null;
    }
    
    private String extractKeyDetails(String message) {
        String detailPrefix = "Detail:";
        if (message.contains(detailPrefix)) {
            int detailIndex = message.indexOf(detailPrefix);
            String detailPart = message.substring(detailIndex + detailPrefix.length()).trim();
            int newlineIndex = detailPart.indexOf('\n');
            if (newlineIndex > 0) { 
                detailPart = detailPart.substring(0, newlineIndex).trim();
            }
            return detailPart;
        }
        return null;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ErrorResponse errorResponse =
        new ErrorResponse(exception.getMessage(), "Invalid JSON data");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<?> handlePSQLException(
            PSQLException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "PSQL error");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(errors.toString(), "Data validation error");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception) {

        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        ErrorResponse errorResponse = new ErrorResponse(errors.toString(), "Data validation error");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "Invalid argument provided");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> handleJsonParseException(JsonParseException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                "Unable to parse JSON: " + (exception.getLocation() != null
                        ? String.format("at line %d, column %d",
                                exception.getLocation().getLineNr(),
                                exception.getLocation().getColumnNr())
                        : "invalid JSON format"));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException exception) {
        log.error("JSON processing error: {}", exception.getMessage());
        String details = "Unable to process JSON";
        if (exception instanceof JsonParseException) {
            JsonParseException parseException = (JsonParseException) exception;
            if (parseException.getLocation() != null) {
                details = String.format("Unable to parse JSON at line %d, column %d",
                        parseException.getLocation().getLineNr(),
                        parseException.getLocation().getColumnNr());
            }
        }
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedToUploadFileException.class)
    public ResponseEntity<?> handleFailedToUploadFileException(FailedToUploadFileException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "Failed to upload file");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileImportValidationException.class)
    public ResponseEntity<?> handleFileImportValidationException(FileImportValidationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                "Failed to validate entities from import file");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnableToGetNecessaryFieldException.class)
    public ResponseEntity<?> handleUnableToGetNecessaryField(UnableToGetNecessaryFieldException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedToProcessImportException.class)
    public ResponseEntity<?> handleFailedToProcessImportException(FailedToProcessImportException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "Failed to process import");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoImportProcessorException.class)
    public ResponseEntity<?> handleNoImportProcessorException(NoImportProcessorException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
        if (exception instanceof TransactionSystemException) {
            throw exception;
        }
        
        if (exception.getCause() instanceof JsonProcessingException) {
            return handleJsonProcessingException((JsonProcessingException) exception.getCause());
        }

        log.error("Unexpected RuntimeException: {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                "Unexpected error during import processing");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<?> handleException(Exception exception) {
    //     log.error("Internal server error: ", exception.getMessage());
    //     return new ResponseEntity<>(
    // "Sorry, something gone wrong on the server side, maybe try again? :( ",
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }

}
