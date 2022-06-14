package com.example.demo.models.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.models.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(path = "api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Endpoint (to refresh your expired access token using your refresh token)", description = "", tags = {"auth"})
    @GetMapping(path = "refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("in refresh getg mapping");
        String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String bearer = "Bearer ";
        if (authorisationHeader != null && authorisationHeader.startsWith(bearer)) {
            try {
                log.info("in refresh getg mapping be");
                String refresh_token = authorisationHeader.substring(bearer.length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();

                Model user = this.userService.findUserByEmail(username);

                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", Arrays.asList(String.format("ROLE_%s", user.getRole())))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
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
            throw new NotFoundException("Refresh token is missing");
        }
    }
}
