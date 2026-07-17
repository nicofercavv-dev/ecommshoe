package com.nicofercavv.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .filter(principal -> principal instanceof Jwt)
                .cast(Jwt.class)
                .flatMap(jwt -> {
                    // Extrai as informações de dentro do JWT do Keycloak
                    String userId = jwt.getSubject(); // ID Único do usuário
                    String email = jwt.getClaimAsString("email");
                    String name = jwt.getClaimAsString("name");

                    // Injeta os dados limpos nos cabeçalhos que vão para o user-service
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Email", email != null ? email : "")
                            .header("X-User-Name", name != null ? name : "")
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                })
                .switchIfEmpty(chain.filter(exchange)); // Se não tiver token (rota pública), segue o fluxo normal
    }

    @Override
    public int getOrder() {
        // Garante que esse filtro rode LOGO APÓS a validação de segurança do Spring
        return Ordered.LOWEST_PRECEDENCE;
    }
}