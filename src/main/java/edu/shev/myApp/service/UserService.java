package edu.shev.myApp.service;

import edu.shev.myApp.domain.User;
import edu.shev.myApp.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService { // сервис для возврата юзера по юзернейму
    //@Autowired
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo){ // замена autowired
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public Optional<User> loadUserById(Long id){
        return userRepo.findById(id);
    }
}
