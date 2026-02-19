package com.bash.LytApp.service.paystack;

import com.bash.LytApp.dto.paystack.PaystackInitializeResponseDto;
import com.bash.LytApp.dto.paystack.PaystackVerificationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaystackAdapter {

    @Value("${paystack-client.secret-key}")
    private String paystackSecretKey;

    private final RestTemplate restTemplate;

    // API Endpoints
    private static final String PAYSTACK_INIT_URL = "https://api.paystack.co/transaction/initialize";
    private static final String PAYSTACK_VERIFY_URL = "https://api.paystack.co/transaction/verify/";

    public PaystackAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaystackInitializeResponseDto initializeTransaction(
            String email,
            BigDecimal amountInKobo,
            String reference,
            String callbackUrl
    ) {
        // 1. Setup Headers
        HttpHeaders headers = createHeaders();

        // 2. Setup Body (Using Map to allow BigDecimal/String amounts)
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        // Using toString() ensures Paystack receives "2500000000" correctly
        // without Java Integer overflow issues.
        body.put("amount", amountInKobo.toBigInteger().toString());
        body.put("reference", reference);
        body.put("callback_url", callbackUrl);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // 3. Make the Call
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    PAYSTACK_INIT_URL,
                    entity,
                    Map.class
            );

            // 4. Validate and Parse
            Map<String, Object> responseBody = response.getBody();
            validatePaystackResponse(responseBody);

            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

            return new PaystackInitializeResponseDto(
                    (String) data.get("authorization_url"),
                    (String) data.get("reference")
            );

        } catch (Exception e) {
            throw new RuntimeException("Paystack Initialization Error: " + e.getMessage(), e);
        }
    }

    public PaystackVerificationResponseDto verifyTransaction(String reference) {
        // 1. Setup Headers
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 2. Make the Call
            String url = PAYSTACK_VERIFY_URL + reference;
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            // 3. Validate and Parse
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || Boolean.FALSE.equals(responseBody.get("status"))) {
                return new PaystackVerificationResponseDto(
                        false,
                        "failed",
                        "Verification failed or reference not found"
                );
            }

            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

            // 4. Extract Status safely
            String status = (String) data.get("status");
            String gatewayResponse = (String) data.get("gateway_response");
            boolean isSuccess = "success".equalsIgnoreCase(status);

            return new PaystackVerificationResponseDto(isSuccess, status, gatewayResponse);

        } catch (Exception e) {
            return new PaystackVerificationResponseDto(false, "error", e.getMessage());
        }
    }

    // Helper to create standard Paystack headers
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(paystackSecretKey);
        return headers;
    }

    // Helper to validate the initial generic response
    private void validatePaystackResponse(Map<String, Object> responseBody) {
        if (responseBody == null) {
            throw new RuntimeException("Empty response from Paystack");
        }
        if (!Boolean.TRUE.equals(responseBody.get("status"))) {
            String message = (String) responseBody.get("message");
            throw new RuntimeException("Paystack Error: " + message);
        }
        if (responseBody.get("data") == null) {
            throw new RuntimeException("Paystack response contains no data");
        }
    }
}