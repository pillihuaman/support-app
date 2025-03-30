package pillihuaman.com.pe.support.foreing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import org.springframework.http.*;

import java.util.Base64;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    // Read the API URL from the configuration
    @Value("${external-api.url}")
    private String securityApiUrl;


    @Autowired
    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MyJsonWebToken fetchData(String apiurl, String tok) {
        return restTemplate.getForObject(securityApiUrl + apiurl + tok, MyJsonWebToken.class);
    }

    /**
     * Valida el token llamando al servicio de seguridad.
     *
     * @param token Token a validar
     * @return True si el token es válido, False si no lo es
     */
    public boolean isTokenValid(String token) {
        System.out.println("Validating token: " + token);

        // Decode and print JWT payload
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            System.out.println("Token Payload: " + payload);
        }

        String url = securityApiUrl + "/api/v1/auth/getUserByToken?request=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Object.class);

            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null;
        } catch (Exception e) {
            return false;
        }
    }
}


