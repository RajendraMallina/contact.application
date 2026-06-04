package itm.codingmaxima.contact.application.service;

import itm.codingmaxima.contact.application.model.AppRole;
import itm.codingmaxima.contact.application.model.PhoneBookUser;
import itm.codingmaxima.contact.application.model.TokenUserDto;
import itm.codingmaxima.contact.application.repository.AppRoleRepository;
import itm.codingmaxima.contact.application.repository.PhoneBookUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private PhoneBookUserRepository phoneBookUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private JWTService jwtService;

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

    public String generateToken(TokenUserDto tokenUserDto){
       PhoneBookUser user = phoneBookUserRepository.findByName(tokenUserDto.getUserName());
       if(user != null){
           return jwtService.generateToken(user);
       }else{
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found to generate token " + tokenUserDto.getUserName());
       }

    }
}
