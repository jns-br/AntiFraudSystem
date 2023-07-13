package antifraud.Exceptions;

import antifraud.Exceptions.User.UserRoleException;
import antifraud.Exceptions.Validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<CustomErrorMessage> handleValidationException(ValidationException e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementExistsException.class)
    public ResponseEntity<CustomErrorMessage> handleElementExistsException(ElementExistsException e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<CustomErrorMessage> handleElementNotFoundException(ElementNotFoundException e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserRoleException.class)
    public ResponseEntity<CustomErrorMessage> handleUserRoleException(UserRoleException e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(e.status.value(), e.getMessage());
        return new ResponseEntity<>(body, e.status);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomErrorMessage> handleUnprocessableEntityException(UnprocessableEntityException e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
