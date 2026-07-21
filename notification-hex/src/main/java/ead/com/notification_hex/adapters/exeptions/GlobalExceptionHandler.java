package ead.com.notification_hex.adapters.exeptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ExternalNotFoundException ex) {
        var errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        log.error("NotFoundException message {} ", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fielname = ((FieldError) error).getField();
            String errorMensagem = error.getDefaultMessage();
            erros.put(fielname, errorMensagem);
        });

        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error validation failed", erros);
        log.error("MethodArgumentNotValidException message {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Map<String, String> erros = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                Reference reference = (JsonMappingException.Reference) ifx.getPath()
                        .get(ifx.getPath().size() - 1);
                String fieldName = reference.getFieldName();
                String errorMessage = ifx.getMessage();
                erros.put(fieldName, errorMessage);
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error Invalid Enum value",
                erros);
        log.error("HttpMessageNotReadableException message {} ", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
