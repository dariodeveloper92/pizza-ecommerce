package com.gateway.security;

import io.jsonwebtoken.Claims; // Aggiungi questo import
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets; // Aggiungi questo import

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final SecretKey secretKey;

    public JwtAuthenticationFilter(@Value("${jwt.secret}") String secret) {
        // CORREZIONE 1: Specifica sempre il Charset per evitare mismatch tra sistemi operativi
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Rotte pubbliche
        if (path.startsWith("/api/users/login") || path.startsWith("/api/users/register")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // CORREZIONE 2: Estraiamo i Claims dal token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            // CORREZIONE 3: "Mutiamo" la richiesta aggiungendo l'header X-User-Email.
            // Questo header verrà letto dal microservizio Cart o Product.
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-User-Email", email)
                            .build())
                    .build();

            System.out.println("Gateway: Token OK. Utente: " + email + " inoltrato a: " + path);

            // Passiamo la richiesta "mutata" alla catena
            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            System.err.println("Gateway: Errore validazione! " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}