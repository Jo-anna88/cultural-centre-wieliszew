package pl.joannaz.culturalcentrewieliszew.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.security.SignatureException;
// java.security.SignatureException

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //io.jsonwebtoken.security.SignatureException (JWT verification)
    @ExceptionHandler({SignatureException.class}) // Exception thrown if there is problem calculating or verifying a digital signature or message authentication code.
    public ResponseEntity<String> handleSignatureException(SignatureException e) {
        logger.error("Signature Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // or UNAUTHORIZED
                .body(e.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        logger.error("Bad Credentials Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error("Entity Not Found Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        logger.error("Username not found Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error("Data Integrity Violation Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

//    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
//    public ResponseEntity<String> handleMethodNotSupportedExceptions(HttpRequestMethodNotSupportedException e) {
//        logger.error("Http Request Method Not Supported Exception: {}", e.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.METHOD_NOT_ALLOWED)
//                .body(e.getMessage());
//    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

//    @ExceptionHandler({Exception.class})
//    public void handleOtherExceptions(Exception e) {
//        logger.error("Other Exception: {}", e.getStackTrace());
//    }
}
