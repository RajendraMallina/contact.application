package itm.codingmaxima.contact.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itm.codingmaxima.contact.application.model.LogInUserDto;
import itm.codingmaxima.contact.application.model.PhoneBookUser;
import itm.codingmaxima.contact.application.service.PhoneBookUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PhoneBookUser Controller", description = "Contact Management APIs")
@RestController
public class PhoneBookUserController {

    @Autowired
    PhoneBookUserService phoneBookUserService;

    @Operation(summary = "Add User To App")
    @PostMapping("/register/user")
    public PhoneBookUser registerNewUser(@RequestBody PhoneBookUser user)
    {
        return phoneBookUserService.register(user);
    }

    @RequestMapping(
            value = {"/login", "/generateToken"},
            method = RequestMethod.POST
    )
    public String generateJWTToken(@RequestBody LogInUserDto logInUserDto){
        return phoneBookUserService.generateLoginToken(logInUserDto);
    }
}
