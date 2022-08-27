    package com.example.springsecurityproject.jwt;

    import com.example.springsecurityproject.entity.TokenEntity;
    import com.example.springsecurityproject.entity.UserEntity;
    import com.example.springsecurityproject.repository.TokenRepository;
    import com.example.springsecurityproject.service.UserService;
    import com.google.common.base.Strings;
    import io.jsonwebtoken.*;
    import org.apache.catalina.User;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.filter.OncePerRequestFilter;

    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.security.Key;

    public class JwtTokenVerifier extends OncePerRequestFilter {
        private final TokenRepository tokenRepository;

        public JwtTokenVerifier(TokenRepository tokenRepository) {
            this.tokenRepository = tokenRepository;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            String authorizationHeader = request.getHeader("Authorization");

            if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer")){
                filterChain.doFilter(request,response);
                return;
            }
            try {
                String token = authorizationHeader.replace("Bearer", "");
                TokenEntity key = tokenRepository.getKeyByUser((UserEntity) request.getUserPrincipal());
                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey((Key) key).build().parseClaimsJws(token);
                Claims body = claimsJws.getBody();
                String username = body.getSubject();
            }
            catch (JwtException e){
                throw new IllegalStateException("Token non verificato");
            }

        }
    }
