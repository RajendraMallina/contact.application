package itm.codingmaxima.contact.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import itm.codingmaxima.contact.application.model.AppRole;
import itm.codingmaxima.contact.application.model.AppUserDetails;
import itm.codingmaxima.contact.application.model.PhoneBookUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${private.key.path}")
    private String privateKeyFilePath;

    @Value("${public.key.path}")
    private String publicKeyFilePath;


    private PrivateKey loadPrivateKey(String privateKeyPath) {
        try {
            Resource resource = new ClassPathResource(privateKeyPath);

            String key = new String(resource.getInputStream().readAllBytes());

            key = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Unable to load RSA private key", e);
        }
    }

    public static PublicKey loadPublicKey(String publicKeyPath) {

        try{
            Resource resource = new ClassPathResource(publicKeyPath);
            String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            key = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String generateToken(PhoneBookUser user){

        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put("email", user.getEmail());
        userClaims.put("roles", user.getRoles()
                                .stream()
                                .map(AppRole::getRoleName)
                                .toList());

        return Jwts.builder()
                .claims(userClaims)
                .subject(user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(loadPrivateKey(privateKeyFilePath))
                .compact();
    }


    public String extractUserName(String jwtToken) {
       return extractClaims(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> typeOfClaim){

        Claims claims = Jwts.parser()
                .verifyWith(loadPublicKey(publicKeyFilePath))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return typeOfClaim.apply(claims);
    }

    public boolean validateToken(String jwtToken, AppUserDetails userDetails) {
        String userName = extractUserName(jwtToken);
        if(userName.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken)){
            return  true;
        }else{
            return false;
        }
    }

    private boolean isTokenExpired(String jwtToken) {
        Date expireDate = extractClaims(jwtToken, Claims::getExpiration);
        return expireDate.before(new Date());
    }
}
