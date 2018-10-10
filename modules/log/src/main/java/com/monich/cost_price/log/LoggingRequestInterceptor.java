package com.monich.cost_price.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MimeType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        String requestBody = new String(body, "UTF-8");
        if (requestBody.isEmpty()) {
            requestBody = "<EMPTY>";
        }
        log.debug("REQUEST to " + request.getURI().toString());
        log.debug("REQUEST method " + request.getMethod());
        log.debug("REQUEST headers: ");
        request.getHeaders().keySet().forEach(k -> log.debug(k + "=" + String.join(",", request.getHeaders().get(k))));

        if (request.getHeaders().getContentType() != null) {
            if (request.getHeaders().getContentType().isCompatibleWith(MimeType.valueOf("multipart/form-data"))) {
                log.debug("REQUEST body: <file>");
            } else {
                log.debug("REQUEST body: " + requestBody);
            }
        }

        ClientHttpResponse response = execution.execute(request, body);
        String responseBody = null;

        MediaType mediaType = response.getHeaders().getContentType();

        boolean mockResponse = false;
        if (mediaType != null) {
            if (mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)
                    || mediaType.isCompatibleWith(MediaType.TEXT_PLAIN)
                    || mediaType.isCompatibleWith(MediaType.TEXT_HTML)) {
                mockResponse = true;
                if (response.getBody() != null) {
                    try {
                        responseBody = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        log.error("IOException", e);
                    }
                }
            } else {
                responseBody = "<stream>";
            }
        }

        final String strBody = responseBody;
        if (responseBody == null || responseBody.isEmpty()) {
            responseBody = "<EMPTY>";
        }

        int responseStatus = response.getStatusCode().value();

        log.debug("RESPONSE status: " + responseStatus);
        response.getHeaders().keySet().forEach(k -> log.debug(k + "=" + String.join(",", response.getHeaders().get(k))));
        log.debug("RESPONSE body: " + responseBody);

        return mockResponse ? new ClientHttpResponse() {
            @Override
            public HttpHeaders getHeaders() {
                return response.getHeaders();
            }

            @Override
            public InputStream getBody() {
                return strBody != null ? new ByteArrayInputStream(strBody.getBytes()) : null;
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return response.getStatusCode();
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return response.getRawStatusCode();
            }

            @Override
            public String getStatusText() throws IOException {
                return response.getStatusText();
            }

            @Override
            public void close() {
                response.close();
            }
        } : response;
    }

}