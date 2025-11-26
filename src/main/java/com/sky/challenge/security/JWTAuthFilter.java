package com.sky.challenge.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.error.ErrorResponse;
import com.sky.challenge.repository.UserRepositoryInterface;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@EnableMethodSecurity
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService service;
    private final UserRepositoryInterface userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "/api/v1/login".equals(path)
                || "/api/v1/user".equals(path)
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            setResponse(response);
            return;
        }

        Jwt jwt = service.getToken(authHeader);

        if (jwt == null || !service.isValid(jwt)) {
            setResponse(response);
            return;
        }

        Optional<User> user = userRepository.findById(java.util.UUID.fromString(jwt.getSubject()));
        if (user.isEmpty()) {
            setResponse(response);
            return;
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user.get(), null, Collections.emptyList());

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(
                new ErrorResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), ErrorMessage.INVALID_TOKEN));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
