package itm.codingmaxima.contact.application.controller;

import itm.codingmaxima.contact.application.model.PhoneBookUser;
import itm.codingmaxima.contact.application.service.PhoneBookUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneBookUserController {

    @Autowired
    PhoneBookUserService phoneBookUserService;

    @PostMapping("/register/user")
    public PhoneBookUser registerNewUser(@RequestBody PhoneBookUser user)
    {
        return phoneBookUserService.register(user);
    }
}
