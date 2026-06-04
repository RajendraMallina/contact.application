package itm.codingmaxima.contact.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import itm.codingmaxima.contact.application.model.AppRole;
import itm.codingmaxima.contact.application.model.PhoneBookUser;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private String secreatKey =  "";

    public JWTService() throws NoSuchAlgorithmException {

        KeyGenerator genKey = KeyGenerator.getInstance("HmacSHA256");
        SecretKey seKey = genKey.generateKey();
        secreatKey = Base64.getEncoder().encodeToString(seKey.getEncoded());
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
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] bytesKey = Base64.getDecoder().decode(secreatKey);
        return Keys.hmacShaKeyFor(bytesKey);
    }

}
