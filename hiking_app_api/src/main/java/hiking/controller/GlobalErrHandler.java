package hiking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("Opps something went wrong :(.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<String> handleException(BadSqlGrammarException ex) {
        return new ResponseEntity<>("Opps something went wrong :(.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
