package pillihuaman.com.pe.support.foreing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProductIds;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ExternalApiService {

    public static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);
    private final RestTemplate restTemplate;


    @Value("${neuro-ia.api.url}")
    private String securityApiNeuroIAUrl;

    @Autowired
    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String cleanJsonResponse(String rawResponse) {
        try {
            logger.debug("Cleaning JSON response");
            if (rawResponse.contains("```json")) {
                return rawResponse.substring(
                        rawResponse.indexOf("```json") + 7,
                        rawResponse.lastIndexOf("```")
                ).trim();
            } else if (rawResponse.contains("```")) {
                return rawResponse.substring(
                        rawResponse.indexOf("```") + 3,
                        rawResponse.lastIndexOf("```")
                ).trim();
            }
            return rawResponse;
        } catch (Exception e) {
            logger.error("Failed to clean JSON response", e);
            return "{\"error\":\"Failed to parse response\",\"raw_response\":\"" +
                    rawResponse.replace("\"", "\\\"") + "\"}";
        }
    }

    private String extractContentFromResponse(Map<String, Object> response) {
        try {
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            logger.warn("No valid content found in response");
            return "No response content found";
        } catch (Exception e) {
            logger.error("Error extracting content from response", e);
            return "Error extracting response content";
        }
    }



    private String extractAndCleanResponse(ResponseEntity<Map> response) {
        try {
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return "{\"error\":\"API returned status: " + response.getStatusCode() + "\"}";
            }

            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String content = (String) message.get("content");

            // Clean the response if it contains markdown
            if (content.contains("```json")) {
                return content.substring(
                        content.indexOf("```json") + 7,
                        content.lastIndexOf("```")
                ).trim();
            } else if (content.contains("```")) {
                return content.substring(
                        content.indexOf("```") + 3,
                        content.lastIndexOf("```")
                ).trim();
            }
            return content;
        } catch (Exception e) {
            logger.error("Error processing API response", e);
            return "{\"error\":\"Failed to process API response\"}";
        }
    }




            private String processVisionResponse(ResponseEntity<Map> response) {
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return "{\"error\":\"API request failed with status: " + response.getStatusCode() + "\"}";
        }

        try {
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");

            if (choices == null || choices.isEmpty()) {
                return "{\"error\":\"No choices in API response\"}";
            }

            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String content = (String) message.get("content");

            // Clean JSON response if it's wrapped in markdown
            return cleanJsonResponse(content);
        } catch (Exception e) {
            logger.error("Error processing vision response", e);
            return "{\"error\":\"Failed to process API response\"}";
        }
    }

    public List<RespFileMetadata> getImagesByProductIds(List<String> productIds, String authToken) {
        logger.info("Iniciando llamada externa a neuro-ia para obtener imágenes de {} productos.", productIds.size());

        // Construir la URL completa del endpoint.
        // Asumiendo que /private/v1/ia/files/ es el path base del otro servicio
        String endpointUrl = securityApiNeuroIAUrl + "/private/v1/ia/files/getCatalogImagesByProducts";

        try {
            // 1. Preparar las cabeceras (Headers)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (authToken != null && !authToken.isEmpty()) {
                headers.set("Authorization", authToken);
            }

            // 2. Preparar el cuerpo de la petición (Request Body)
            ReqProductIds requestBody = new ReqProductIds(productIds,true);
            HttpEntity<ReqProductIds> entity = new HttpEntity<>(requestBody, headers);

            logger.debug("Realizando llamada POST a: {}", endpointUrl);
            logger.debug("Request Body: {}", requestBody);

            // 3. Realizar la llamada POST usando RestTemplate
            ResponseEntity<List<RespFileMetadata>> response = restTemplate.exchange(
                    endpointUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<RespFileMetadata>>() {} // Esperamos una lista de RespFileMetadata
            );

            // 4. Procesar y devolver la respuesta
            if (response.getStatusCode() == HttpStatus.OK) {
                List<RespFileMetadata> files = response.getBody();
                logger.info("Llamada exitosa. Se obtuvieron metadatos de {} imágenes.", files != null ? files.size() : 0);
                return files != null ? files : Collections.emptyList();
            } else {
                logger.warn("La llamada a neuro-ia no tuvo éxito. Status HTTP: {}", response.getStatusCode());
                return Collections.emptyList();
            }

        } catch (HttpClientErrorException e) {
            logger.error("Error del cliente (4xx) al llamar a neuro-ia. Status: {}. Body: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error inesperado al conectar con el servicio neuro-ia en URL: {}", endpointUrl, e);
            return Collections.emptyList();
        }
    }
    public String getSignedUrlForKey(String s3Key, String authToken) {
        String endpointUrl = securityApiNeuroIAUrl + "/private/v1/ia/files/generate-url/" + s3Key;
        try {
            HttpHeaders headers = new HttpHeaders();
            if (authToken != null && !authToken.isEmpty()) {
                headers.set("Authorization", authToken);
            }
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    endpointUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, String>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().get("url");
            }
            return null;
        } catch (Exception e) {
            logger.error("Error al obtener URL firmada para key: {}", s3Key, e);
            return null;
        }
    }

}