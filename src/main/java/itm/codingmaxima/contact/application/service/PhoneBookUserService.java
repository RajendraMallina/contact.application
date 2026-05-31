package itm.codingmaxima.contact.application.service;

import itm.codingmaxima.contact.application.model.PhoneBookUser;
import itm.codingmaxima.contact.application.repository.PhoneBookUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PhoneBookUserService {

    @Autowired
    private PhoneBookUserRepository phoneBookUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PhoneBookUser register(PhoneBookUser user) {

        if (phoneBookUserRepository.findById(user.getId()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already available"
            );
        }

        if (phoneBookUserRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        phoneBookUserRepository.save(user);
        return user;
    }
}
