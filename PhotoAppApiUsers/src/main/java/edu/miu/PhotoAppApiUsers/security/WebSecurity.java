package edu.miu.PhotoAppApiUsers.security;

import edu.miu.PhotoAppApiUsers.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private Environment envi;

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(Environment envi, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.envi = envi;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authBuilder.build();
        // Create AuthenticationFilter
        AuthenticationFilter authenticationFilter = new AuthenticationFilter( authenticationManager,userService, envi);
        authenticationFilter.setFilterProcessesUrl(envi.getProperty("login.url.path"));


        http.csrf().disable();
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,"/users/**").permitAll()
//                .access(new WebExpressionAuthorizationManager("hasIpAddress('"+envi.getProperty("gateway.ip")+"')"))

                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager, envi))
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        return http.build();
    }
}
