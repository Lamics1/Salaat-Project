package com.finalproject.tuwaiqfinal.Advise;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.exception.SdkClientException;
import org.springframework.mail.MailException;
import kong.unirest.UnirestException;
import java.io.IOException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;

@ControllerAdvice
public class ControllerAdvise {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvise.class);

    // Our Exception
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> ApiException(ApiException apiException) {
        String message = apiException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // SQL Constraint Ex:(Duplicate) Exception
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException) {
        String message = sqlIntegrityConstraintViolationException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // Server Validation Exception
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        HashMap<String, String> errorMessages = new HashMap<>();
        for (FieldError error : methodArgumentNotValidException.getFieldErrors()) {
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    // Server Validation Exception
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> ConstraintViolationException(ConstraintViolationException constraintViolationException) {
        String message = constraintViolationException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // wrong write SQL in @column Exception
    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ApiResponse> InvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
        String message = invalidDataAccessResourceUsageException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // Database Constraint Exception
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> DataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException) {
        String message = dataIntegrityViolationException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // Json parse Exception
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> HttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        String message = httpMessageNotReadableException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // Method not allowed Exception
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        String message = httpRequestMethodNotSupportedException.getMessage();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ApiResponse(message));
    }

    // TypesMisMatch Exception
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        String message = methodArgumentTypeMismatchException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
    }

    // Non-Existing Route Exception
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<?> NoResourceFoundException(NoResourceFoundException noResourceFoundException) {
        String message = noResourceFoundException.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(message));
    }

    // Generic Exception Handler - catch any unexpected exceptions
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {
        // Log the exception for debugging (in production, use proper logging)
        logger.error("Unexpected error: {}", exception.getMessage(), exception);

        // Return 500 Internal Server Error for unexpected exceptions
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("An unexpected error occurred. Please try again later."));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> MultipartException(MultipartException MultipartException){
        return ResponseEntity.status(400).body(MultipartException.getMessage());
    }

    @ExceptionHandler(NoSuchKeyException.class)
    public ResponseEntity<?> NoSuchKeyException(NoSuchKeyException NoSuchKeyException){
        return ResponseEntity.status(400).body(NoSuchKeyException.getMessage());
    }

    // AWS S3 Service Exception
    @ExceptionHandler(value = software.amazon.awssdk.services.s3.model.S3Exception.class)
    public ResponseEntity<ApiResponse> handleAmazonS3Exception(software.amazon.awssdk.services.s3.model.S3Exception e) {
        logger.error("AWS S3 service error: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(new ApiResponse("AWS S3 service error: " + e.getMessage()));
    }

    // AWS SDK Client Exception
    @ExceptionHandler(value = software.amazon.awssdk.core.exception.SdkClientException.class)
    public ResponseEntity<ApiResponse> handleSdkClientException(software.amazon.awssdk.core.exception.SdkClientException e) {
        logger.error("AWS SDK client error: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(new ApiResponse("AWS S3 client error: " + e.getMessage()));
    }

    // Mail Exception
    @ExceptionHandler(value = MailException.class)
    public ResponseEntity<ApiResponse> handleMailException(MailException e) {
        logger.error("Mail error: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(new ApiResponse("Failed to send email: " + e.getMessage()));
    }

    // Unirest Exception
    @ExceptionHandler(value = UnirestException.class)
    public ResponseEntity<ApiResponse> handleUnirestException(UnirestException e) {
        logger.error("Unirest error: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(new ApiResponse("External service communication error: " + e.getMessage()));
    }

    // IO Exception
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ApiResponse> handleIOException(IOException e) {
        logger.error("IO error: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(new ApiResponse("File operation error: " + e.getMessage()));
    }

    // Data Access Exception
    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<ApiResponse> handleDataAccessException(DataAccessException e) {
        logger.error("Data access error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Database access error: " + e.getMessage()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        String message = "Access Denied: You do not have permission to access this resource.\n"+accessDeniedException.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException authenticationException) {
        String message = "Authentication failed: " + authenticationException.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(message));
    }



}
