package com.example.bajajfinserv.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void executeFlow() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Chintalagattu Abhiram");
        requestBody.put("regNo", "22BEC7110");
        requestBody.put("email", "abhiram.22bec7110@vitapstudent.ac.in");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        System.out.println("Webhook Generation Response: " + response);
        if (response.getBody() == null) {
            System.out.println("No response body from webhook generation API.");
            return;
        }
        String webhookUrl = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");
        System.out.println("webhookUrl: " + webhookUrl);
        System.out.println("accessToken: " + accessToken);

        String finalQuery = "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e.DEPARTMENT AND e2.DOB > e.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e.EMP_ID DESC;";

        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("solution", finalQuery);

        HttpHeaders answerHeaders = new HttpHeaders();
        answerHeaders.setContentType(MediaType.APPLICATION_JSON);
        answerHeaders.set("Authorization", "Bearer " + accessToken);
        answerHeaders.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Map<String, String>> answerEntity = new HttpEntity<>(answerBody, answerHeaders);

        try {
            ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, answerEntity, String.class);
            System.out.println("Submission Response: " + submitResponse);
        } catch (Exception ex) {
            System.out.println("Error during submission: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
