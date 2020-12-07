package com.bhp.opusb.service.trigger.process.integration;

import java.util.Map;

import com.bhp.opusb.config.ApplicationProperties;
import com.bhp.opusb.service.dto.ProcessResult;
import com.bhp.opusb.service.dto.TriggerResult;
import com.bhp.opusb.service.trigger.ProcessTrigger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("mVerificationMessageDispatcher")
public class MVerificationMessageDispatcher implements ProcessTrigger {

  private static final Logger logger = LoggerFactory.getLogger(MVerificationMessageDispatcher.class);

  public static final String KEY_CONTEXT = "context";
  public static final String KEY_PAYLOAD = "payload";
  public static final String CONTEXT_HEADER = "invoice-verification-header";
  public static final String CONTEXT_LINES = "invoice-verification-lines";

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper;
  private final ApplicationProperties properties;

  public MVerificationMessageDispatcher(ObjectMapper objectMapper, ApplicationProperties properties) {
    this.objectMapper = objectMapper;
    this.properties = properties;
  }

  @Override
  public TriggerResult run(Map<String, Object> params) {
    final Object payload = params.get(KEY_PAYLOAD);
    final String context = (String) params.get(KEY_CONTEXT);
    boolean success = true;
    String response = null;

    if (context != null && payload != null) {
      String message = null;

      try {
        message = objectMapper.writeValueAsString(params);
        response = dispatchMessage(message, context);
      } catch (JsonProcessingException e) {
        logger.error("Failed dispatching invoice verification. {}. {}", e.getLocalizedMessage(), payload);
        success = false;
      }

      return new ProcessResult().add("success", success).add("response", success ? response : payload);
    }

    return new ProcessResult().add("error", true).add("message",
        "There is no context or payload in the given params. " + params);
  }

  private String dispatchMessage(String message, String context) {
    final String url = properties.getIntegration().getEndpoint().getInvoiceVerificationUrl();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("ai-exchange-out", context);

    logger.debug("Dispatching context#{} to the external system. payload: {}", context, message);
    HttpEntity<String> request = new HttpEntity<>(message, headers);
    final ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    logger.debug("Message dispatched. statusCode: {} - {}", response.getStatusCodeValue(), response.getStatusCode());
    return response.getBody();
  }

}