package com.scopevisio.praemiepro.web.mvc;

import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.service.UserService;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DashboardMVC {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @RequestMapping(value = "/dashboard")
    public String dashboard(final Model model) {
        final User currentUser = userService.getCurrentUser().orElseThrow();
        final List<String> authorities = currentUser.getAuthorities().stream().map(Authority::getName).toList();
        final List<OrderDTO> orders = findOrdersBasedOnAuthorities(authorities, currentUser);

        model.addAttribute("vehicleTypes", VehicleType.values());
        model.addAttribute("user", currentUser);
        model.addAttribute("auth", authorities);
        model.addAttribute("orders", orders);

        return "dashboard";
    }

    private List<OrderDTO> findOrdersBasedOnAuthorities(final List<String> authorities, final User user) {
        final List<Order> orders = authorities.contains("ROLE_ADMIN")
                ? orderRepository.findAll()
                : orderRepository.findAllByUser_id(user.getId());

        return orderMapper.ordersToOrderDTOs(orders);
    }
}
