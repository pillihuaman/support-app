package pillihuaman.com.pe.support.foreing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.NotificationRequestDTO;


/**
 * Cliente HTTP para comunicarse con el Microservicio de Notificaciones.
 * Si la comunicación falla, lanza una MicroserviceComunicationException.
 */
@Service
public class NotificationServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${external-api.url}")
    private String securityApiBaseUrl;

    @Autowired
    public NotificationServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Llama al endpoint para encolar una nueva notificación.
     *
     * @param notificationDto El DTO con los detalles de la notificación.
     * @param authToken       El token de autorización.
     * @return Un RespBase si la llamada es exitosa.
     * @throws MicroserviceComunicationException Si ocurre un error de comunicación.
     */
    public RespBase<Object> sendNotification(NotificationRequestDTO notificationDto, String authToken) {
        String endpointUrl = securityApiBaseUrl + "/api/v1/notifications/send";
        logger.info("Enviando notificación a [{}], destinatario: {}", endpointUrl, notificationDto.getRecipient());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null && !authToken.isBlank()) {
            headers.set("Authorization", authToken);
        } else {
            logger.warn("Llamada a notificaciones SIN token de autorización.");
        }

        RespBase<NotificationRequestDTO> requestBody = new RespBase<>();
        requestBody.setPayload(notificationDto);

        HttpEntity<RespBase<NotificationRequestDTO>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<RespBase> responseEntity = restTemplate.exchange(
                    endpointUrl,
                    HttpMethod.POST,
                    requestEntity,
                    RespBase.class);
            logger.info("Notificación encolada exitosamente.");
            return responseEntity.getBody();

    }
}