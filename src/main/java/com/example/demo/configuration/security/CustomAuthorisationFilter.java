package com.example.demo.configuration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class CustomAuthorisationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("qq");
        System.out.println("in filter");
        if (request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/refresh") || request.getServletPath().equals("/api/candidates/item") | request.getServletPath().equals("/api/candidates/translation") | request.getServletPath().equals("/api/jobs/translation") | request.getServletPath().equals("/api/recruiters/translation")) {
            filterChain.doFilter(request, response);
            log.info("pp");
        } else {
            log.info("zz");
            String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String bearer = "Bearer ";
            if (authorisationHeader != null && authorisationHeader.startsWith(bearer)) {
                try {
                    String token = authorisationHeader.substring(bearer.length());
                    log.info(token);
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    System.out.println("roles.length");
                    System.out.println(roles.length);
                    for (String r: roles) {
                        System.out.println(r);
                    }
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> { authorities.add(new SimpleGrantedAuthority(role)); });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("error_message", exception.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                }
            } else {
                System.out.println("no autho header");
                filterChain.doFilter(request, response);
            }
        }
    }
}
