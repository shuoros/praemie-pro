package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.UserNotFoundException;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.service.dto.InsuranceDTO;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import com.scopevisio.praemiepro.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private InsuranceCalculatorService insuranceCalculatorService;

    @Autowired
    private OrderMapper orderMapper;

    public OrderDTO createOrder(final VehicleType vehicleType,
                             final Integer yearlyDrive,
                             final String zipcode,
                             final User user) {
        final InsuranceDTO insuranceDTO = insuranceCalculatorService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                zipcode
        );

        return orderMapper.orderToOrderDTO(
                saveOrder(vehicleType, yearlyDrive, zipcode, insuranceDTO, user)
        );
    }

    public OrderDTO registerOrder(final VehicleType vehicleType, final Integer yearlyDrive, final String zipcode) {
        final InsuranceDTO insuranceDTO = insuranceCalculatorService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                zipcode
        );
        final User currentUser = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);

        return orderMapper.orderToOrderDTO(
                saveOrder(vehicleType, yearlyDrive, zipcode, insuranceDTO, currentUser)
        );
    }

    public List<OrderDTO> findOrdersOfUser(final UserDTO userDTO) {
        final List<Order> ordersOfUser = userDTO != null
                ? orderRepository.findAllByUser_id(userDTO.getId())
                : Collections.emptyList();

        return orderMapper.ordersToOrderDTOs(ordersOfUser);
    }

    public List<OrderDTO> findOrdersBasedOnAuthorities(final List<String> authorities, final User user) {
        final List<Order> orders = authorities.contains("ROLE_ADMIN")
                ? orderRepository.findAll()
                : orderRepository.findAllByUser_id(user.getId());

        return orderMapper.ordersToOrderDTOs(orders);
    }

    private Order saveOrder(final VehicleType vehicleType,
                            final Integer yearlyDrive,
                            final String zipcode,
                            final InsuranceDTO insuranceDTO,
                            final User user) {
        final Order order = new Order();
        order.setVehicleType(vehicleType);
        order.setYearlyDrive(yearlyDrive);
        order.setZipcode(zipcode);
        order.setYearlyPrice(insuranceDTO.getYearlyPrice());
        order.setUser(user);
        return orderRepository.save(order);
    }
}
