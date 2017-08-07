package com.witkups.repobrowser.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
@Order
final class GlobalExceptionHandler {

    private static final String ON_WRONG_REQUEST_MESSAGE = "Not recognized request!";
    private static final String ON_UNKNOWN_ERROR_MESSAGE = "An error occurred";
    private static final String ON_UNSUPPORTED_MEDIA_TYPE_FORMAT = "%s, allowed: %s";
    private static final String ON_HTTP_ERROR_MESSAGE = "Http error";
    private static final String ON_HTTP_MEDIA_TYPE_ERROR_MESSAGE = "Http Media Type error";


    @ExceptionHandler(HttpStatusCodeException.class)
    ErrorData onHttpException(HttpServletResponse response, HttpStatusCodeException ex) {
        final String message = ex.getLocalizedMessage();
        final int responseStatus = ex.getRawStatusCode();
        response.setStatus(responseStatus);
        log.error(ON_HTTP_ERROR_MESSAGE, ex);
        return new ErrorData(message);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    ErrorData onHttpMediaTypeException(HttpMediaTypeException ex) {
        final String message = getUnsupportedMediaTypeMessage(ex);
        log.error(ON_HTTP_MEDIA_TYPE_ERROR_MESSAGE, ex);
        return new ErrorData(message);
    }

    private String getUnsupportedMediaTypeMessage(HttpMediaTypeException ex) {
        return String.format(
                ON_UNSUPPORTED_MEDIA_TYPE_FORMAT,
                ex.getLocalizedMessage(),
                ex.getSupportedMediaTypes()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorData onWrongRequest(NoHandlerFoundException ex) {
        final ErrorData errorData = new ErrorData(ON_WRONG_REQUEST_MESSAGE);
        log.error(ON_WRONG_REQUEST_MESSAGE, ex);
        return errorData;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorData onException(Exception ex) {
        final ErrorData errorData = new ErrorData(ON_UNKNOWN_ERROR_MESSAGE);
        log.error(ON_UNKNOWN_ERROR_MESSAGE, ex);
        return errorData;
    }



}
