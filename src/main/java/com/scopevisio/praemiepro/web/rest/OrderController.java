package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.security.IsAdmin;
import com.scopevisio.praemiepro.security.IsUser;
import com.scopevisio.praemiepro.service.OrderService;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.web.vm.CalculateVM;
import com.scopevisio.praemiepro.web.vm.OrderUpdateVM;
import com.scopevisio.praemiepro.web.vm.OrderVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

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

    @PatchMapping("/{id}")
    @IsUser
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderUpdateVM orderUpdateVM) {
        if (!Objects.equals(id, orderUpdateVM.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        final Optional<OrderDTO> orderDTO = orderService.updateOrder(
                orderUpdateVM.getId(),
                orderUpdateVM.getVehicleType(),
                orderUpdateVM.getYearlyDrive(),
                orderUpdateVM.getZipcode()
        );

        return ResponseEntity.ok(
                orderDTO.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
