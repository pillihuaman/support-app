package pillihuaman.com.pe.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ResponseUser;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Extrae el nombre de usuario (email) del token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Función genérica para extraer una "claim" específica del token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Valida un token JWT. Comprueba si el nombre de usuario coincide y si el token no ha expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Comprueba si un token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Parsea y valida el token para extraer toda la información ("claims").
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma a partir de la clave secreta en formato base64.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Parsea un token en formato "Bearer <token>" a tu objeto personalizado MyJsonWebToken.
     * ESTE ES EL MÉTODO QUE TU AwsController USA.
     */
    public MyJsonWebToken parseTokenToMyJsonWebToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null; // Token inválido o ausente
        }

        String token = bearerToken.substring(7);
        Claims claims;
        try {
            claims = extractAllClaims(token);
        } catch (Exception e) {
            return null; // Token expirado o con firma inválida
        }

        MyJsonWebToken myJsonWebToken = new MyJsonWebToken();

        // 1. EXTRAER TODOS LOS DATOS DEL NIVEL RAÍZ DE LOS CLAIMS
        Map<String, Object> userMap = (Map<String, Object>) claims.get("user");
        String tenantIdFromToken = claims.get("tenantId", String.class);
        List<Map<String, Object>> rolesFromToken = (List<Map<String, Object>>) claims.get("role");
        Map<String, Object> applicationFromToken = (Map<String, Object>) claims.get("application");

        // 2. PROCESAR Y CONSTRUIR EL OBJETO ResponseUser
        if (userMap != null) {
            ResponseUser user = new ResponseUser();

            // Poblar campos simples del usuario
            Object rawId = userMap.get("id");
            if (rawId != null) user.setId(new ObjectId(rawId.toString()));

            Object rawEmail = userMap.get("email");
            if (rawEmail != null) user.setMail(rawEmail.toString());

            Object rawAlias = userMap.get("alias");
            if (rawAlias != null) user.setAlias(rawAlias.toString());

            // Asignar tenantId (limpiando las comillas dobles si aún existen)
            if (tenantIdFromToken != null) {
                user.setTenantId(tenantIdFromToken.replace("\"", ""));
            }

            // --- TRANSFORMACIÓN DE ROLES (Solución al Error 1) ---
            if (rolesFromToken != null && !rolesFromToken.isEmpty()) {
                List<String> roleNames = rolesFromToken.stream()
                        .map(roleMap -> (String) roleMap.get("name")) // Extraer solo el campo "name"
                        .toList(); // Convertir el stream a una lista de Strings
                user.setRoles(roleNames);
            }

            myJsonWebToken.setUser(user);
        }

        // --- TRANSFORMACIÓN DE APLICATION (Solución al Error 2) ---
        if (applicationFromToken != null) {
            MyJsonWebToken.Application app = new MyJsonWebToken.Application();
            Object appID = applicationFromToken.get("applicationID");

            // Suponemos que el ID de la aplicación también es un ObjectId en el origen,
            // pero se guarda como String en el token.
            if(appID != null) {
            //    app.setAplicationID(new ObjectId(appID.toString()));
            }

            // Si el token incluyera más campos como "name" o "multiSession", los asignaríamos aquí.
            // app.setName((String) applicationFromToken.get("name"));

            myJsonWebToken.setAplication(app);
        }

        return myJsonWebToken;
    }
}