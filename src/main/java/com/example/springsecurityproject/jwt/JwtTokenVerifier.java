    package com.example.springsecurityproject.jwt;

    import com.example.springsecurityproject.entity.TokenEntity;
    import com.example.springsecurityproject.entity.UserEntity;
    import com.example.springsecurityproject.repository.TokenRepository;
    import com.example.springsecurityproject.service.UserService;
    import com.google.common.base.Strings;
    import io.jsonwebtoken.*;
    import io.jsonwebtoken.io.Decoders;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.filter.OncePerRequestFilter;
    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.util.List;
    import java.util.Map;
    import java.util.Set;
    import java.util.stream.Collectors;


    /**
     * This will be our JwtTokenVerifier that will - as the name suggest - verify our token for every request has been
     * submitted to the server.
     */
    public class JwtTokenVerifier extends OncePerRequestFilter {
        private final TokenRepository tokenRepository;
        private final UserService userService;

        public JwtTokenVerifier(TokenRepository tokenRepository, UserService userService) {
            this.tokenRepository = tokenRepository;
            this.userService = userService;
        }

        /**
         * This method is going to be executed for every request submitted to the server, it'll check the headers of the
         * request and will take 2 values from it if it exists, if it doesn't exist, the request is going to be FORBIDDEN.
         * The "Authorization" header will store our JWT token created on "JwtUsernameAndPasswordAuthenticationFilter" filter
         * The "Authenticated" header will store the Base64 String of the username that made the request, that will be used
         * to get the key stored at the login in the Token_Entity table.
         * After some conditions, if the token is verified it'll return our requested page (if you have authorization to see that page)
         * if it's not verified it'll throw a JwtException.
         *
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @param filterChain FilterChain
         * @throws ServletException Exception
         * @throws IOException Exception
         */
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
                String username = claimsJws.getBody().getSubject();
                var authorities = (List<Map<String, String>>) claimsJws.getBody().get("authorities");
                Set<SimpleGrantedAuthority> authority = authorities.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authority
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (JwtException e){
                throw new IllegalStateException("Token non verificato");
            }
            filterChain.doFilter(request,response);
        }
    }
