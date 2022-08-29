    package com.example.springsecurityproject.jwt;

    import com.example.springsecurityproject.entity.TokenEntity;
    import com.example.springsecurityproject.entity.UserEntity;
    import com.example.springsecurityproject.repository.TokenRepository;
    import com.example.springsecurityproject.service.UserService;
    import com.google.common.base.Strings;
    import io.jsonwebtoken.*;
    import io.jsonwebtoken.io.Decoders;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;


    public class JwtTokenVerifier extends OncePerRequestFilter {
        private final TokenRepository tokenRepository;
        private final UserService userService;

        public JwtTokenVerifier(TokenRepository tokenRepository, UserService userService) {
            this.tokenRepository = tokenRepository;
            this.userService = userService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            String authorizationHeader = request.getHeader("Authorization");
            String authorizatedHeader = request.getHeader("Authenticated");

            if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer") || Strings.isNullOrEmpty(authorizatedHeader)){
                filterChain.doFilter(request,response);
                return;
            }
            try {
                String token = authorizationHeader.replace("Bearer", "");
                String encryptedUser = authorizatedHeader.replace("AuthString","");
                byte [] decryptedUser = Decoders.BASE64.decode(encryptedUser);
                String finalUser = new String(decryptedUser);
                TokenEntity instance = tokenRepository.getKeyByUser((UserEntity) userService.loadUserByUsername(finalUser));
                byte[] key = Decoders.BASE64.decode(instance.getToken());
                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

                System.out.println(claimsJws.getHeader() + " " + claimsJws.getBody() + " " + claimsJws.getSignature());

            }
            catch (JwtException e){
                throw new IllegalStateException("Token non verificato");
            }

        }
    }
