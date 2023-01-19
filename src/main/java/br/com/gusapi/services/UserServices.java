package br.com.gusapi.services;

import br.com.gusapi.model.User;
import br.com.gusapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServices implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    UserRepository repository;

    public UserServices(UserRepository repository) {

        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name!");
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
