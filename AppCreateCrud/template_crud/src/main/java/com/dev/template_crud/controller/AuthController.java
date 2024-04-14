package com.dev.template_crud.controller;

// import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.CookieGenerator;

import com.dev.template_crud.model.User;
import com.dev.template_crud.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private  UserService userService;

    @GetMapping
    public String login(){
        return "login";
    }

    @GetMapping("/home")
    public String home(){
        return "index";
    }

    @SuppressWarnings("deprecation")
    @PostMapping("/auth")
    public  String authentify(@RequestParam("email")String email,@RequestParam("password")String password,Model model,HttpServletResponse response )throws Exception{
        int login = userService.login(email, password);
        // int typeLogin = 0;
        switch (login) {
            case 1:
                User user= userService.getUser(email);

                CookieGenerator cookieGenerator=new CookieGenerator();
                cookieGenerator.setCookieName("cookie_user");
                cookieGenerator.addCookie(response, userService.genereToken(user.getId()).toString());
                return "redirect:/models/list";
            case 0:
                model.addAttribute("title","Mot de passe incorrect");
                model.addAttribute("erreur", "Mot de passe incorrect");
                return "/auth/login";
            case -1:
                model.addAttribute("title","Addresse email incorrect");
                model.addAttribute("erreur", "Addresse email incorrect");
                return "/auth/login";
        }
        return null;

    }
    // public  String authentify(@RequestParam("email")String email,@RequestParam("password")String password,Model model,HttpServletResponse response )throws Exception{
    //     int login = userService.login(email, password);
    //     int typeLogin = 0;
    //      switch (login) {
    //         case 1:
    //             User user= userService.getUser(email);

    //             CookieGenerator cookieGenerator=new CookieGenerator();
    //             cookieGenerator.setCookieName("cookie_user");
    //             cookieGenerator.addCookie(response, userService.genereToken(user.getId()).toString());
    //             return "redirect:/login/home";
    //         case 0:
    //             model.addAttribute("title","Mot de passe incorrect");
    //             model.addAttribute("erreurPassword", "Mot de passe incorrect");
    //             return "/auth/login";
    //         case -1:
    //             typeLogin = 1;
    //             break;
    //         case -3:
    //             typeLogin=-1;
    //             break;
    //         default:
    //             typeLogin=-1;
    //             break;
    //     }
    //     if(typeLogin==1){
    //         if(login==1){
    //             return "redirect:/produit";
    //         }
    //         else if(login==0){
    //             model.addAttribute("title","Mot de passe incorrect");
    //             model.addAttribute("erreurPassword", "Mot de passe incorrect");
    //             return "redirect:/auth/login";

    //         }
    //         else{
    //             model.addAttribute("title","Email ou identifiant inconnue");
    //             model.addAttribute("erreurIdentifiant", "Email ou identifiant inconnue");
    //             return "redirect:/auth/login";
    //         }
    //     }
    //     else{
    //         model.addAttribute("title","Compte banni");
    //         model.addAttribute("erreurIdentifiant", "Ce compte a été suspendu");
    //         return "redirect:/auth/login";
    //     }
    // }
    
}