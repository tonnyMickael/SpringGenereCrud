package com.dev.template_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.template_crud.service.TokenService;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/models")
public class ModelsController {

    @Autowired
	private TokenService tokenService;

    // @GetMapping("/list")
    // public String listModels(@CookieValue(value = "cookie_user", defaultValue = "") String coockieUser){
    //     if(tokenService.verifyCoockieUser(coockieUser)){
    //         return "models-list";
    //     }
    //     else {
    //         return"redirect:/login";
    //     }
    // }
    @GetMapping("/list")
    public String listModels(){
        return "models-list";
    }
}
