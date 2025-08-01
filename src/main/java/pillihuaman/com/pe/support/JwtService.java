package pillihuaman.com.pe.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.HashMap;
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
            // Manejar el caso de un token inválido o ausente
            return null;
        }

        String token = bearerToken.substring(7);
        Claims claims = extractAllClaims(token);

        MyJsonWebToken myJsonWebToken = new MyJsonWebToken();
        Map<String, Object> userMap = (Map<String, Object>) claims.get("user");

        if (userMap != null) {
            ResponseUser user = new ResponseUser();
            user.setId(new ObjectId(userMap.get("id").toString()));
            user.setMail(userMap.get("email").toString());
            // Puedes añadir más campos si los necesitas (alias, mobilPhone, etc.)
            myJsonWebToken.setUser(user);
        }
        // Aquí también podrías parsear la información de "aplication" si está en los claims
        return myJsonWebToken;
    }
}