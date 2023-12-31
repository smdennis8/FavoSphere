package favorite.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String ISSUER = "favorite";
    private final int EXPIRATION_MINUTES = 15;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    public String getTokenFromUser(AppUser user) {

        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getEmail())
                .claim("app_user_id", user.getAppUserId())
                .claim("first_name", user.getFirstName())
                .claim("middle_name", user.getMiddleName())
                .claim("last_name", user.getLastName())
                .claim("phone", user.getPhone())
                .claim("email", user.getEmail())
                .claim("password", user.getPassword())
                .claim("registered_on", String.valueOf(user.getRegisteredOn()))
                .claim("last_login", String.valueOf(user.getLastLogin()))
                .claim("user_enabled", user.isEnabled())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public AppUser getUserFromToken(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));

            BigInteger appUserId = new BigInteger(jws.getBody().get("app_user_id").toString());
            String firstName = (String) jws.getBody().get("first_name");
            String middleName = (String) jws.getBody().get("middle_name");
            String lastName = (String) jws.getBody().get("last_name");
            String phone = (String) jws.getBody().get("phone");
            String email = (String) jws.getBody().get("email");
            String password = (String) jws.getBody().get("password");
            LocalDate registeredOn = LocalDate.parse(jws.getBody().get("registered_on").toString());
            LocalDate lastLogin = LocalDate.parse(jws.getBody().get("last_login").toString());
            Boolean userEnabled = (Boolean) jws.getBody().get("user_enabled");
            String authStr = (String) jws.getBody().get("authorities");

            return new AppUser(appUserId, firstName, middleName, lastName, phone,
                    email, password, registeredOn, lastLogin, userEnabled,
                    Arrays.asList(authStr.split(",")));

        } catch (JwtException e) {
            System.out.println(e);
        }

        return null;
    }
}