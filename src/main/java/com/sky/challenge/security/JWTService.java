package com.sky.challenge.security;

import com.sky.challenge.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public String generateToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getId().toString())
                .build();

        var encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(SignatureAlgorithm.RS256).build(), claims);

        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public Jwt getToken(String token) {
        return decoder.decode(token.substring(7));
    }

    public boolean isValid(Jwt jwt) {
        return jwt.getExpiresAt() != null && Instant.now().isBefore(jwt.getExpiresAt());
    }
}
