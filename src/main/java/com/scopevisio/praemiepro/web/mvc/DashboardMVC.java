package com.scopevisio.praemiepro.web.mvc;

import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.security.IsAdmin;
import com.scopevisio.praemiepro.service.OrderService;
import com.scopevisio.praemiepro.service.UserService;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DashboardMVC {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/dashboard")
    public String dashboard(final Model model) {
        final User currentUser = userService.getCurrentUser().orElseThrow();
        final List<String> authorities = currentUser.getAuthorities().stream().map(Authority::getName).toList();
        final List<OrderDTO> orders = orderService.findOrdersBasedOnAuthorities(authorities, currentUser);

        model.addAttribute("vehicleTypes", VehicleType.values());
        model.addAttribute("user", currentUser);
        model.addAttribute("auth", authorities);
        model.addAttribute("orders", orders);

        return "dashboard";
    }

    @RequestMapping(value = "/dashboard/users")
    @IsAdmin
    public String users(final Model model) {
        final List<UserDTO> usersDTOs = userService.findCustomerUsers();

        model.addAttribute("users", usersDTOs);

        return "users";
    }

    @RequestMapping(value = "/dashboard/users/{id}")
    @IsAdmin
    public String user(final Model model, @PathVariable("id") final Long id) {
        final UserDTO userDTO = userService.findUserDTOById(id).orElse(null);
        final List<OrderDTO> ordersOfUser = orderService.findOrdersOfUser(userDTO);

        model.addAttribute("vehicleTypes", VehicleType.values());
        model.addAttribute("user", userDTO);
        model.addAttribute("orders", ordersOfUser);

        return "user";
    }
}
