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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {


    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userEditFormSave(
            @RequestParam("userId") User user,     // тут по параметру userId мы получаем всего юзера
            @RequestParam(name = "roles[]", required = false) String[] roles,
            @RequestParam String username) {
        user.setUsername(username);
        user.getRoles().clear();
        Set<String> actRoles = Arrays.stream(roles).collect(Collectors.toSet());

        Set<Role> actRols = new HashSet<>();

        for (String role : actRoles) {
            actRols.add(Role.valueOf(role));
        }
        user.setRoles(actRols);
        userRepo.save(user);
        return "redirect:/user";
    }


}
