package net.digitallogic.UserLogin.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.web.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final String tokenSecret;

    @Autowired
    public AuthorizationFilter(AuthenticationManager authenticationManager,
                               UserRepository userRepository,
                               String tokenSecret) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenSecret = tokenSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        Cookie accessCookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);

        if (accessCookie != null) {
            String token = accessCookie.getValue();
            if (token != null) {
                String userId = Jwts.parser()
                        .setSigningKey(tokenSecret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                log.debug("User: {}", userId);
                if (userId != null) {
                    try {
                        UserEntity userEntity = userRepository.findByUserIdWithAuthorities(userId);
                        UserPrincipal user = new UserPrincipal(userRepository.findByUserIdWithAuthorities(userId));
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
                        );
                    } catch(EntityNotFoundException ex) {
                        log.info("Token references invalid user account: {}", userId);
                    }
                }
            }
        }


        log.info("Request URI is: {}", request.getRequestURI());
        chain.doFilter(request, response);
    }

//    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
//        Cookie accessCookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);
//        if (accessCookie != null) {
//            String token = accessCookie.getValue();
//            log.info("Received token from cookie {}", token);
//
//            if (token != null) {
//
//                String user = Jwts.parser()
//                        .setSigningKey(SecurityConstants.TOKEN_SECRET)
//                        .parseClaimsJws(token)
//                        .getBody()
//                        .getSubject();
//                if (user != null) {
//                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
//                }
//            }
//        }
//        return null;
//    }
}
