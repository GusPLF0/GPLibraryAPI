package br.com.gusapi.services;

import br.com.gusapi.data.vo.v1.security.AccountCredentialsVO;
import br.com.gusapi.data.vo.v1.security.TokenVO;
import br.com.gusapi.model.User;
import br.com.gusapi.repositories.UserRepository;
import br.com.gusapi.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity signIn(AccountCredentialsVO data) {
        try {
            String username = data.getUsername();
            String password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

            var tokenResponse = new TokenVO();

            tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    public ResponseEntity refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

        var tokenResponse = new TokenVO();

        tokenResponse = tokenProvider.refreshToken(refreshToken);

        return ResponseEntity.ok(tokenResponse);
    }

}
