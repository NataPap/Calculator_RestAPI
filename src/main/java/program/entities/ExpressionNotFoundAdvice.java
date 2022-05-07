package program.entities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExpressionNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ExpressionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String expressionNotFoundHandler(ExpressionNotFoundException ex) {
        return ex.getMessage();
    }
}