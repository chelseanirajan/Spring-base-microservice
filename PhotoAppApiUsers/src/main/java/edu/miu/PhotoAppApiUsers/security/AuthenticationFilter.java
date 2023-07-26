package edu.miu.PhotoAppApiUsers.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.PhotoAppApiUsers.dto.UserDTO;
import edu.miu.PhotoAppApiUsers.model.LoginRequestModel;
import edu.miu.PhotoAppApiUsers.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authentication, UserService userService, Environment environment){
        super(authentication);
        this.userService =userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){

        try {
            LoginRequestModel loginReq= new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword(),
                    new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,Authentication auth){
        String userName = ((User)auth.getPrincipal()).getUsername();
        UserDTO userDTO = userService.getUserDetailsByEmail(userName);
        String tokenSecret = environment.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey= new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        String token = Jwts.builder()
                .setSubject(userDTO.getUserId())
                .setExpiration(Date.from(Instant.now().plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
        res.addHeader("token", token);
        res.addHeader("userId", userDTO.getUserId());

    }
}
