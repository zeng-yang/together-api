package com.zhlzzz.together.rank;

import lombok.experimental.UtilityClass;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@UtilityClass
public class HttpUtils {

    public ResponseEntity<Object> getPubgResponse(String endpointUrl, String apiKey, Map<String,String> urlVariables){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Authorization","Bearer "+apiKey);
        headers.add("Accept","application/vnd.api+json");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        return restTemplate.exchange(endpointUrl, HttpMethod.GET, entity, Object.class,urlVariables);
    }
}
