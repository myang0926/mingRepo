package service;
import org.apache.commons.codec.binary.Base64;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.bind.DatatypeConverter;

@Component
public class TokenProvider {

    @Autowired
    private AppConfig config;

    public String createToken(String username) {
        byte[] secretKey = DatatypeConverter.parseBase64Binary(config.getSecret());
        long tokenValidityInMilliseconds = 1000 * config.getTokenValidityInSeconds();
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username)
                .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(validity).compact();
    }


}