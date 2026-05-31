package itm.codingmaxima.contact.application.service;

import itm.codingmaxima.contact.application.model.PhoneBookUser;
import itm.codingmaxima.contact.application.model.UserPrincipal;
import itm.codingmaxima.contact.application.repository.PhoneBookUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PhoneBookUserDetailsService implements UserDetailsService {

    @Autowired
    PhoneBookUserRepository phoneBookUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PhoneBookUser user = phoneBookUserRepository.findByName(username);
        if (user == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }

        return new UserPrincipal(user);
    }
}
