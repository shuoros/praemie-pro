package com.scopevisio.praemiepro.web.mvc;

import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexMVC {

    @RequestMapping(value = "/")
    public String index(final Model model) {
        model.addAttribute("vehicleTypes", VehicleType.values());

        return "index";
    }
}
