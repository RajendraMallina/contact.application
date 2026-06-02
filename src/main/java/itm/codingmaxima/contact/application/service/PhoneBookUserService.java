package itm.codingmaxima.contact.application.service;

import itm.codingmaxima.contact.application.model.AppRole;
import itm.codingmaxima.contact.application.model.AppUserDetails;
import itm.codingmaxima.contact.application.model.LogInUserDto;
import itm.codingmaxima.contact.application.model.PhoneBookUser;
import itm.codingmaxima.contact.application.repository.AppRoleRepository;
import itm.codingmaxima.contact.application.repository.PhoneBookUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhoneBookUserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;
    @Autowired
    private PhoneBookUserRepository phoneBookUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppRoleRepository appRoleRepository;

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

        List<Integer> roleIds = new ArrayList<>();
        for(AppRole role : user.getRoles()){
            roleIds.add(role.getId());
        }
        Set<AppRole> roles = new HashSet<>(
                appRoleRepository.findAllById(roleIds)
        );

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        phoneBookUserRepository.save(user);
        return user;
    }

    public String generateLoginToken(LogInUserDto logInUserDto) {
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                logInUserDto.getUserName(),
                                logInUserDto.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {

            AppUserDetails userDetails =
                    (AppUserDetails) authentication.getPrincipal();

            PhoneBookUser phoneBookUser = userDetails.getUser();

            return jwtService.generateToken(phoneBookUser);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token Generation failed");
        }
    }
}
