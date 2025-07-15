package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.security.IsAdmin;
import com.scopevisio.praemiepro.security.IsUser;
import com.scopevisio.praemiepro.service.OrderService;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.web.vm.CalculateVM;
import com.scopevisio.praemiepro.web.vm.OrderVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    @IsAdmin
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderVM orderVM) {
        final OrderDTO order = orderService.createOrder(
                orderVM.getVehicleType(),
                orderVM.getYearlyDrive(),
                orderVM.getZipcode(),
                orderVM.getUser()
        );

        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getId()))
                .body(order);
    }

    @PostMapping("/register")
    @IsUser
    public ResponseEntity<OrderDTO> registerOrder(@Valid @RequestBody CalculateVM calculateVM) {
        final OrderDTO order = orderService.registerOrder(
                calculateVM.getVehicleType(),
                calculateVM.getYearlyDrive(),
                calculateVM.getZipcode()
        );

        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getId()))
                .body(order);
    }
}
