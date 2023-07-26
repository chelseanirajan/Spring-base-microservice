package edu.miu.AccountManagement.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountManagerController {

    @GetMapping("/management")
    public String getAccount(){
        return "Account Management";
    }
}
