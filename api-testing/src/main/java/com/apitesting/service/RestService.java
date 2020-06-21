package com.apitesting.service;

import com.apitesting.model.CapturedFlow;
import com.apitesting.model.Response;
import org.apache.http.HttpException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class RestService {
    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Response sendRequest(CapturedFlow request) {
        String url = request.getUrl();
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        HttpHeaders headers = generateHeaders(request.getHeaders().replaceAll("'", "\""));

        if (Objects.isNull(headers)) {
            return null;
        }

        HttpEntity<String> entity = new HttpEntity(request.getContent(), headers);
        Response response = new Response();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);

            response.setBody(responseEntity.getBody());
            response.setStatusCode(responseEntity.getStatusCodeValue());
        } catch (HttpStatusCodeException ex) {
            response.setBody(ex.getResponseBodyAsString());
            response.setStatusCode(ex.getRawStatusCode());
        }

        return response;
    }

    public HttpHeaders generateHeaders(String headersString) {
        HttpHeaders headers = new HttpHeaders();

        try {
            JSONObject headersObject = new JSONObject(headersString);

            headersObject.keySet().forEach(key -> {
                headers.set(key, headersObject.getString(key));
            });

            return headers;
        } catch (Exception ex) {
            return null;
        }
    }
}
