package net.digitallogic.UserLogin.config;

import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.security.AuthenticationFilter;
import net.digitallogic.UserLogin.security.AuthorizationFilter;
import net.digitallogic.UserLogin.security.SecurityConstants;
import net.digitallogic.UserLogin.service.UserService;
import net.digitallogic.UserLogin.web.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final UserRepository userRepository;
    private final String tokenSecret;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserService userService, UserRepository userRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                          @Value("${token.secret}") String tokenSecret) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenSecret = tokenSecret;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    //    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, Routes.SIGN_UP_URL)
                .permitAll()

                .antMatchers(SecurityConstants.H2_CONSOLE)
                .permitAll()

                .anyRequest()
                .authenticated()
                .and()

                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), userRepository, tokenSecret))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();
    }

    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), tokenSecret);
       // filter.setFilterProcessesUrl(Routes.USERS + Routes.LOGIN);
        filter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(Routes.USERS + Routes.LOGIN, HttpMethod.POST.name())
        );

        return filter;
    }
}
