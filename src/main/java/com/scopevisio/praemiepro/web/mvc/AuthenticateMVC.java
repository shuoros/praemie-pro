package com.scopevisio.praemiepro.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenticateMVC {

    @RequestMapping(value = "/authenticate")
    public String authenticate() {
        return "authenticate";
    }
}
