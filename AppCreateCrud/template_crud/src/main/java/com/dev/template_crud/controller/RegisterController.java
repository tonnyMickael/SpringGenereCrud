package com.dev.template_crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.template_crud.model.User;
import com.dev.template_crud.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showRegister(Model theModel) {
        User user = new User();
        theModel.addAttribute("user", user);
        return "register";
    }

     @PostMapping("/add")
    public String save(@ModelAttribute("user") User user) {
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "redirect:/login";
    }
     @GetMapping("/list")
    public String listUser(Model theModel) {
        List < User > theUsers = userService.getAllUser();
        theModel.addAttribute("user", theUsers);
        return "register-list";
    }

}
