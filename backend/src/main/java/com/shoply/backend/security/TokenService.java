package com.shoply.backend.security;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.shoply.backend.user.model.Usuario;

@Service
public class TokenService {
    
    private final JwtEncoder jwtEncoder;
    private final Duration expiracao;
    private final String issuer;

    public TokenService(
        JwtEncoder jwtEncoder,
        @Value("${security.jwt.expiration}") Duration expiracao,
        @Value("${security.jwt.issuer}") String issuer
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expiracao = expiracao;
        this.issuer = issuer;
    }

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .subject(usuario.getId().toString())
            .issuedAt(agora)
            .expiresAt(agora.plus(expiracao))
            .id(UUID.randomUUID().toString())
            .claim("perfil", usuario.getPerfil().name())
            .build();

        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .build();

        return jwtEncoder.encode(
            JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public long getExpiracaoEmSegundos() {
        return expiracao.toSeconds();
    }
}
