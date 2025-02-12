package edu.shev.myApp.controller;

import edu.shev.myApp.domain.Role;
import edu.shev.myApp.domain.User;
import edu.shev.myApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepo.findAll());

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userEditFormSave(
            @RequestParam("userId") User user,     // тут по параметру userId мы получаем всего юзера
            @RequestParam(name="roles[]", required = false) String[] roles,
            @RequestParam String username)
    {
        user.setUsername(username);
        user.getRoles().clear();
        if(roles!= null){
            Arrays.stream(roles).forEach(r -> user.getRoles().add(Role.valueOf(r)));
        }
        userRepo.save(user);
        return "redirect:/user";
    }

}
