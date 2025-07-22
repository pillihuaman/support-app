package pillihuaman.com.pe.support.foreing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import pillihuaman.com.pe.lib.common.RespBase;

import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class NeuroIaFileStorageServiceImpl implements NeuroIaFileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(NeuroIaFileStorageServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    // Añade esta URL en tu application.properties del servicio 'support'
    // neuro-ia.api.url=http://localhost:8081 (o la URL del servicio neuroIA)
    @Value("${neuro-ia.api.url}")
    private String neuroIaApiUrl;
    @Override
    public RespBase<List<RespFileMetadata>> uploadFilesToNeuroIA(MultipartFile[] files, String metadataJson, String entityId, String authToken) {

        String url = UriComponentsBuilder.fromHttpUrl(neuroIaApiUrl)
                .path("/private/v1/ia/files/upload")
                .queryParam("productId", entityId)
                .toUriString();

        logger.info("Llamando al servicio externo de subida de archivos en: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // --- INICIO DE LA CORRECCIÓN ---
        // Lógica robusta para manejar el token de autorización.
        if (authToken != null && !authToken.isEmpty()) {
            String tokenValue = authToken.trim();

            // 1. Limpia cualquier prefijo "Bearer " existente, sin importar mayúsculas/minúsculas o repeticiones.
            //    Ej: "Bearer bearer token" -> "token"
            while (tokenValue.toLowerCase().startsWith("bearer ")) {
                tokenValue = tokenValue.substring(7).trim();
            }

            // 2. Añade el prefijo "Bearer " correcto para asegurar el formato estándar.
            headers.set("Authorization", "Bearer " + tokenValue);
            logger.debug("Cabecera de autorización establecida correctamente.");
        }
        // --- FIN DE LA CORRECCIÓN ---

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("metadata", metadataJson);

        if (files != null) {
            for (MultipartFile file : files) {
                body.add("files", file.getResource());
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<List<RespFileMetadata>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<RespFileMetadata>>() {}
            );

            logger.info("Respuesta exitosa recibida del servicio neuroIA.");

            return RespBase.<List<RespFileMetadata>>builder()
                    .payload(response.getBody())
                    .status(new RespBase.Status(true, null))
                    .build();

        } catch (HttpClientErrorException e) {
            logger.error("Error del cliente al llamar al servicio neuroIA: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            RespBase.Status.Error error = RespBase.Status.Error.builder()
                    .code(String.valueOf(e.getStatusCode().value()))
                    .messages(List.of("Error del servicio de archivos: " + e.getResponseBodyAsString()))
                    .build();
            return RespBase.<List<RespFileMetadata>>builder()
                    .status(new RespBase.Status(false, error))
                    .build();
        } catch (Exception e) {
            logger.error("Error inesperado al llamar al servicio neuroIA", e);
            RespBase.Status.Error error = RespBase.Status.Error.builder()
                    .code("500")
                    .messages(List.of("Error interno de conexión con el servicio de archivos: " + e.getMessage()))
                    .build();
            return RespBase.<List<RespFileMetadata>>builder()
                    .status(new RespBase.Status(false, error))
                    .build();
        }
    }

    @Override
    public void deleteFileFromNeuroIA(String fileId, String authToken) {
        // Construye la URL para el endpoint de eliminación en el servicio neuroIA
        String url = UriComponentsBuilder.fromHttpUrl(neuroIaApiUrl)
                .path("/private/v1/ia/files/{id}")
                .buildAndExpand(fileId)
                .toUriString();

        logger.info("Llamando al servicio externo para eliminar el archivo en: {}", url);

        // Configura los encabezados, incluyendo el token de autorización
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null && !authToken.isEmpty()) {
            headers.set("Authorization", authToken.startsWith("Bearer ") ? authToken : "Bearer " + authToken);
        }
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // Realiza la llamada DELETE. Esperamos una respuesta sin cuerpo (Void.class).
            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class // No se espera un cuerpo en la respuesta
            );
            logger.info("Solicitud de eliminación para el archivo ID {} enviada exitosamente.", fileId);

        } catch (HttpClientErrorException e) {
            // Si el servicio de destino devuelve un error (ej. 404 Not Found), lo registramos.
            logger.error("Error del cliente al eliminar el archivo ID {}: {} - {}",
                    fileId, e.getStatusCode(), e.getResponseBodyAsString());
            // No relanzamos la excepción para no detener el borrado de otros archivos.
        } catch (Exception e) {
            // Para cualquier otro error (ej. de conexión)
            logger.error("Error inesperado al intentar eliminar el archivo ID {}", fileId, e);
        }
    }

    @Override
    public RespBase<Map<String, Object>> getPresignedUrl(String s3Key, String typeFile, String authToken) {
        // 1. Construir la URL del endpoint en neuroIA
        String url = UriComponentsBuilder.fromHttpUrl(neuroIaApiUrl)
                .path("/private/v1/ia/files/generate-presigned-url")
                .queryParam("key", s3Key)
                .queryParam("typeFile", typeFile)
                // Opcional: podrías añadir la duración si quisieras controlarla desde aquí
                // .queryParam("durationInMinutes", 15)
                .toUriString();

        logger.info("Llamando a neuroIA para generar URL pre-firmada: {}", url);

        // 2. Configurar los encabezados (Headers)
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Lógica para añadir el token de autorización (reutilizada de tus otros métodos)
        if (authToken != null && !authToken.isEmpty()) {
            String tokenValue = authToken.trim();
            if (!tokenValue.toLowerCase().startsWith("bearer ")) {
                tokenValue = "Bearer " + tokenValue;
            }
            headers.set("Authorization", tokenValue);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // 3. Realizar la llamada GET y manejar la respuesta
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            logger.info("URL pre-firmada obtenida exitosamente de neuroIA.");

            // Construir la respuesta exitosa
            return RespBase.<Map<String, Object>>builder()
                    .payload(response.getBody())
                    .status(new RespBase.Status(true, null))
                    .build();

        } catch (HttpClientErrorException e) {
            logger.error("Error del cliente al llamar a neuroIA para generar URL: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            RespBase.Status.Error error = RespBase.Status.Error.builder()
                    .code(String.valueOf(e.getStatusCode().value()))
                    .messages(List.of("Error del servicio de firma de archivos: " + e.getResponseBodyAsString()))
                    .build();
            return RespBase.<Map<String, Object>>builder()
                    .status(new RespBase.Status(false, error))
                    .build();
        } catch (Exception e) {
            logger.error("Error inesperado al llamar a neuroIA para generar URL", e);
            RespBase.Status.Error error = RespBase.Status.Error.builder()
                    .code("500")
                    .messages(List.of("Error interno de conexión con el servicio de firma: " + e.getMessage()))
                    .build();
            return RespBase.<Map<String, Object>>builder()
                    .status(new RespBase.Status(false, error))
                    .build();
        }
    }
}