package com.witkups.repobrowser.github;

import com.witkups.repobrowser.error.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GitHubExceptionHandler {

    private static final String REPOSITORY_NOT_FOUND_MESSAGE = "Could not find requested repository";
    private static final String LOG_MESSAGE_FORMAT = "response status: %d";

    @ExceptionHandler(HttpStatusCodeException.class)
    public ErrorData onHttpClientException(
            HttpServletResponse response,
            HttpClientErrorException exception) {
        final int statusCode = exception.getRawStatusCode();
        final String logMessage = getLogMessage(statusCode);
        log.error(
                logMessage, exception);
        response.setStatus(statusCode);
        final String message = prepareHttpExceptionMessage(statusCode, exception);
        return new ErrorData(message);
    }

    private String getLogMessage(int statusCode) {
        return String.format(LOG_MESSAGE_FORMAT, statusCode);
    }

    private String prepareHttpExceptionMessage(int statusCode, HttpClientErrorException ex) {
        if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
            return REPOSITORY_NOT_FOUND_MESSAGE;
        } else {
            return ex.getLocalizedMessage();
        }
    }

}
